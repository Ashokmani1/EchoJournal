package com.sample.echojournal.ui.model

import com.sample.echojournal.domain.model.Mood

data class JournalUi(
    val id: Int,
    val title: String,
    val description: String,
    val selectedMood: Mood,
    val date: String,
    val time: String,
    val topics: List<String>
)