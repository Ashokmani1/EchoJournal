package com.sample.echojournal.ui.screens.journal

import androidx.compose.ui.graphics.Path
import com.sample.echojournal.domain.model.Mood

sealed interface JournalListAction
{
    data class SelectMood(val mood: Mood): JournalListAction
    data class SelectTopic(val topic: String): JournalListAction

    data class DeselectMood(val mood: Mood): JournalListAction
    data class DeselectTopic(val topic: String): JournalListAction

    data class RecordingComplete(val audioPath: String): JournalListAction
    data class PlayAudio(val id: String): JournalListAction

    data object ClearMoodFilter: JournalListAction
    data object ClearTopicFilter: JournalListAction

    data object StopRecording: JournalListAction
    data object StopAudio: JournalListAction

    data object StartRecording: JournalListAction
    data object PauseRecording: JournalListAction
    data object DiscardRecording: JournalListAction

    data class CanShowSheetToRecord(val show: Boolean): JournalListAction
}