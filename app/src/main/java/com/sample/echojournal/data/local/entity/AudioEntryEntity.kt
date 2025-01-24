package com.sample.echojournal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "audio_entries")
data class AudioEntryEntity(
    @PrimaryKey val id: String,
    val timestamp: LocalDateTime,
    val audioPath: String,
    val title: String,
    val description: String?,
    val mood: String,
    val topics: List<String>,
    val duration: Long,
    val waveformData: List<Float>
)