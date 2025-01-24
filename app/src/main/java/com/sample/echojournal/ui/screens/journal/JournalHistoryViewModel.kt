package com.sample.echojournal.ui.screens.journal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.echojournal.domain.audio.AudioPlayer
import com.sample.echojournal.domain.model.AudioEntry
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JournalHistoryViewModel @Inject constructor(
    private val repository: AudioRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    data class JournalHistoryUiState(
        val entries: List<AudioEntry> = emptyList(),
        val selectedMoods: Set<Mood> = emptySet(),
        val selectedTopics: Set<String> = emptySet(),
        val isRecording: Boolean = false,
        val currentlyPlayingId: String? = null,
        val error: String? = null,
        val lastRecordedPath: String? = null
    )

    private val _uiState = MutableStateFlow(JournalHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            repository.getAllEntries()
                .catch { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
                .collect { entries ->
                    _uiState.update { it.copy(entries = entries) }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMoodFilter(mood: Mood) {
        _uiState.update { state ->
            val newMoods = state.selectedMoods.toMutableSet()
            if (newMoods.contains(mood)) newMoods.remove(mood) else newMoods.add(mood)
            state.copy(selectedMoods = newMoods)
        }
        filterEntries()
    }

    fun updateTopicFilter(topic: String) {
        _uiState.update { state ->
            val newTopics = state.selectedTopics.toMutableSet()
            if (newTopics.contains(topic)) newTopics.remove(topic) else newTopics.add(topic)
            state.copy(selectedTopics = newTopics)
        }
        filterEntries()
    }


    fun playAudio(entryId: String)
    {
        viewModelScope.launch {
            try {
                val entry = repository.getEntryById(entryId)
                entry?.let { audioEntry ->
                    if (entryId == uiState.value.currentlyPlayingId)
                    {
                        // Toggle pause/resume if same entry
                        audioPlayer.pause()
                        _uiState.update { it.copy(currentlyPlayingId = null) }
                    } else {
                        // Start playing new entry
                        audioPlayer.play(audioEntry.audioPath)
                        _uiState.update { it.copy(currentlyPlayingId = entryId) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message,
                    currentlyPlayingId = null
                )}
            }
        }
    }

    fun stopAudio() {
        audioPlayer.stop()
        _uiState.update { it.copy(currentlyPlayingId = null) }
    }

    fun startRecording()
    {
        _uiState.update { it.copy(isRecording = true) }
    }

    fun stopRecording() {
        _uiState.update { it.copy(isRecording = false) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterEntries() {
        viewModelScope.launch {
            val moods = _uiState.value.selectedMoods
            val topics = _uiState.value.selectedTopics
            
            repository.getAllEntries()
                .map { entries ->
                    entries.filter { entry ->
                        (moods.isEmpty() || moods.contains(entry.mood)) &&
                        (topics.isEmpty() || entry.topics.any { it in topics })
                    }
                    .groupBy { entry ->
                        when {
                            entry.timestamp.toLocalDate() == LocalDate.now()              -> "Today"
                            entry.timestamp.toLocalDate() == LocalDate.now().minusDays(1) -> "Yesterday"
                            else -> entry.timestamp.format(DateTimeFormatter.ofPattern("EEEE, MMM dd"))
                        }
                    }
                }
                .collect { grouped ->
                    _uiState.update { it.copy(entries = grouped.values.flatten()) }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleRecordingComplete(audioPath: String) {
        viewModelScope.launch {
            try {
                val entry = AudioEntry(
                    id = UUID.randomUUID().toString(),
                    title = "",
                    description = null,
                    audioPath = audioPath,
                    mood = Mood.NEUTRAL,
                    topics = emptyList(),
                    timestamp = LocalDateTime.now(),
                    duration = 0L,
                    waveformData = emptyList()
                )
                repository.insertEntry(entry)
                _uiState.update { it.copy(
                    isRecording = false,
                    lastRecordedPath = audioPath
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}