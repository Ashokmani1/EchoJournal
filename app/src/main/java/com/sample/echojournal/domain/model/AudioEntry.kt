package com.sample.echojournal.domain.model

import androidx.compose.ui.graphics.Color
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
    val iconResId: Int,
    val unSelectedIconResId: Int
) {
    Stressed(
        R.drawable.ic_stressed,
        R.drawable.ej_ic_mood_stress_in_active
    ),
    Sad(
        R.drawable.ic_sad,
        R.drawable.ej_ic_mood_sad_in_active
    ),
    Neutral(
        R.drawable.ic_neutral,
        R.drawable.ej_ic_mood_neutral_in_active
    ),
    Peaceful(
        R.drawable.ic_peaceful,
        R.drawable.ej_ic_mood_peaceful_in_active
    ),
    Excited(
        R.drawable.ic_excited,
        R.drawable.ej_ic_mood_excited_in_active
    )
}

fun Mood.getMoodColor(): Color
{
    return when (this) {
        Mood.Stressed -> Color(0xFFDE3A3A)
        Mood.Sad -> Color(0xFF3A8EDE)
        Mood.Neutral -> Color(0xFF41B278)
        Mood.Peaceful -> Color(0xFFBE3294)
        Mood.Excited -> Color(0xFFDB6C0B)
    }
}

fun Mood.getMoodIconResId(): Int {
    return when (this) {
        Mood.Stressed -> R.drawable.ic_stressed
        Mood.Sad -> R.drawable.ic_sad
        Mood.Neutral -> R.drawable.ic_neutral
        Mood.Peaceful -> R.drawable.ic_peaceful
        Mood.Excited -> R.drawable.ic_excited
    }
}

fun Mood.getMoodBgColor(): Color
{
    return when (this) {
        Mood.Stressed -> Color(0xFFF8EFEF)
        Mood.Sad -> Color(0xFFEFF4F8)
        Mood.Neutral -> Color(0xFFEEF7F3)
        Mood.Peaceful -> Color(0xFFF6F2F5)
        Mood.Excited -> Color(0xFFF5F2EF)
    }
}