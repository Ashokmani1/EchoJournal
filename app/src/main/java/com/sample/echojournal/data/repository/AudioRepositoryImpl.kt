package com.sample.echojournal.data.repository

import com.sample.echojournal.data.local.dao.AudioEntryDao
import com.sample.echojournal.data.mapper.toAudioEntry
import com.sample.echojournal.data.mapper.toAudioEntryEntity
import com.sample.echojournal.domain.model.AudioEntry
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val audioEntryDao: AudioEntryDao
) : AudioRepository {
    
    override fun getAllEntries(): Flow<List<AudioEntry>> =
        audioEntryDao.getAllEntries().map { entities ->
            entities.map { it.toAudioEntry() }
        }

    override fun getEntriesByMood(moods: List<Mood>): Flow<List<AudioEntry>> =
        audioEntryDao.getEntriesByMood(moods.map { it.name })
            .map { entities -> entities.map { it.toAudioEntry() } }

    override fun getEntriesByTopic(topic: String): Flow<List<AudioEntry>> =
        audioEntryDao.getEntriesByTopic(topic)
            .map { entities -> entities.map { it.toAudioEntry() } }

    override suspend fun insertEntry(entry: AudioEntry) {
        audioEntryDao.insertEntry(entry.toAudioEntryEntity())
    }

    override suspend fun deleteEntry(entry: AudioEntry) {
        audioEntryDao.deleteEntry(entry.toAudioEntryEntity())
    }

    override suspend fun getEntryById(entryId: String): AudioEntry?
    {
        return audioEntryDao.getEntryById(entryId)?.toAudioEntry()
    }

}