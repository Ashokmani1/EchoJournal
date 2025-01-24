package com.sample.echojournal.data.repository

import com.sample.echojournal.data.local.dao.SettingsDao
import com.sample.echojournal.data.local.entity.SettingsEntity
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override fun getDefaultMood(): Flow<Mood?> =
        settingsDao.getSettings().map { settings ->
            settings?.defaultMood?.let { Mood.valueOf(it) }
        }

    override fun getDefaultTopics(): Flow<List<String>> =
        settingsDao.getSettings().map { settings ->
            settings?.defaultTopics ?: emptyList()
        }

    override suspend fun updateDefaultMood(mood: Mood)
    {
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: SettingsEntity(
            defaultMood = mood.name,
            defaultTopics = emptyList()
        )
        settingsDao.updateSettings(currentSettings.copy(defaultMood = mood.name))
    }

    override suspend fun updateDefaultTopics(topics: List<String>)
    {
        val currentSettings = settingsDao.getSettings().firstOrNull() ?: SettingsEntity(
            defaultMood = Mood.NEUTRAL.name,
            defaultTopics = topics
        )
        settingsDao.updateSettings(currentSettings.copy(defaultTopics = topics))
    }

    override suspend fun clearSettings() {
        settingsDao.clearSettings()
    }
}