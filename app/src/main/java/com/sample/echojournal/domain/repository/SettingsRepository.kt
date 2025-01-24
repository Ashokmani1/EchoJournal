package com.sample.echojournal.domain.repository

import com.sample.echojournal.domain.model.Mood
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDefaultMood(): Flow<Mood?>
    fun getDefaultTopics(): Flow<List<String>>
    suspend fun updateDefaultMood(mood: Mood)
    suspend fun updateDefaultTopics(topics: List<String>)
    suspend fun clearSettings()
}