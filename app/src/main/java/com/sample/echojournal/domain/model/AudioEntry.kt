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
    val iconResId: Int,
    val unSelectedIconResId: Int
) {
    STRESSED(
        androidx.compose.ui.graphics.Color(0xFFDE3A3A),
        R.drawable.ic_stressed,
        R.drawable.ej_ic_mood_stress_in_active
    ),
    SAD(
        androidx.compose.ui.graphics.Color(0xFF3A8EDE),
        R.drawable.ic_sad,
        R.drawable.ej_ic_mood_sad_in_active
    ),
    NEUTRAL(
        androidx.compose.ui.graphics.Color(0xFF41B278),
        R.drawable.ic_neutral,
        R.drawable.ej_ic_mood_neutral_in_active
    ),
    PEACEFUL(
        androidx.compose.ui.graphics.Color(0xFFBE3294),
        R.drawable.ic_peaceful,
        R.drawable.ej_ic_mood_peaceful_in_active
    ),
    EXCITED(
        androidx.compose.ui.graphics.Color(0xFFDB6C0B),
        R.drawable.ic_excited,
        R.drawable.ej_ic_mood_excited_in_active
    )
}