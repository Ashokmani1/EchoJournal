package com.sample.echojournal.domain.model

import com.sample.echojournal.R
import java.time.LocalDateTime

data class AudioEntry(
    val id: String,
    val timestamp: LocalDateTime,
    val audioPath: String,
    val title: String,
    val description: String?,
    val mood: Mood,
    val topics: List<String>,
    val duration: Long,
    val waveformData: List<Float>
)

enum class Mood(
    val color: androidx.compose.ui.graphics.Color,
    val iconResId: Int
) {
    STRESSED(
        androidx.compose.ui.graphics.Color.Red,
        R.drawable.ic_stressed
    ),
    SAD(
        androidx.compose.ui.graphics.Color.Blue,
        R.drawable.ic_sad
    ),
    NEUTRAL(
        androidx.compose.ui.graphics.Color.Green,
        R.drawable.ic_neutral
    ),
    PEACEFUL(
        androidx.compose.ui.graphics.Color(0xFFFF69B4),
        R.drawable.ic_peaceful
    ),
    EXCITED(
        androidx.compose.ui.graphics.Color(0xFFFFA500),
        R.drawable.ic_excited
    )
}