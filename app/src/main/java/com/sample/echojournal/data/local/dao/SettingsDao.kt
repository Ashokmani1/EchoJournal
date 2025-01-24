package com.sample.echojournal.data.local.dao

import androidx.room.*
import com.sample.echojournal.data.local.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao
{
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: SettingsEntity)

    @Query("DELETE FROM settings")
    suspend fun clearSettings()
}