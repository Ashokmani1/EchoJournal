package com.sample.echojournal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.echojournal.domain.model.Mood

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val defaultMood: String,
    val defaultTopics: List<String>
)