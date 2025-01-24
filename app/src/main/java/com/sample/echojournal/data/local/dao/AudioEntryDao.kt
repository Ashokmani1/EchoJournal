package com.sample.echojournal.data.local.dao

import androidx.room.*
import com.sample.echojournal.data.local.entity.AudioEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioEntryDao
{
    @Query("SELECT * FROM audio_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<AudioEntryEntity>>

    @Query("SELECT * FROM audio_entries WHERE mood IN (:moods) ORDER BY timestamp DESC")
    fun getEntriesByMood(moods: List<String>): Flow<List<AudioEntryEntity>>

    @Query("SELECT * FROM audio_entries WHERE topics LIKE '%' || :topic || '%' ORDER BY timestamp DESC")
    fun getEntriesByTopic(topic: String): Flow<List<AudioEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: AudioEntryEntity)

    @Query("SELECT * FROM audio_entries WHERE id = :entryId")
    suspend fun getEntryById(entryId: String): AudioEntryEntity?

    @Delete
    suspend fun deleteEntry(entry: AudioEntryEntity)
}