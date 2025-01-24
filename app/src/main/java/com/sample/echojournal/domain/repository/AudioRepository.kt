package com.sample.echojournal.domain.repository

import com.sample.echojournal.domain.model.AudioEntry
import com.sample.echojournal.domain.model.Mood
import kotlinx.coroutines.flow.Flow

interface AudioRepository {
    fun getAllEntries(): Flow<List<AudioEntry>>
    fun getEntriesByMood(moods: List<Mood>): Flow<List<AudioEntry>>
    fun getEntriesByTopic(topic: String): Flow<List<AudioEntry>>
    suspend fun insertEntry(entry: AudioEntry)
    suspend fun deleteEntry(entry: AudioEntry)
    suspend fun getEntryById(entryId: String): AudioEntry?
}