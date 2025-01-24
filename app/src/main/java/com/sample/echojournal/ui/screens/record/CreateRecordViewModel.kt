package com.sample.echojournal.ui.screens.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.echojournal.domain.model.AudioEntry
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.repository.AudioRepository
import com.sample.echojournal.domain.audio.AudioRecorder
import com.sample.echojournal.domain.repository.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateRecordViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val topicRepository: TopicRepository,
    private val audioRecorder: AudioRecorder
) : ViewModel() {
   
    data class UiState(
        val title: String = "",
        val description: String = "",
        val mood: Mood = Mood.NEUTRAL,
        val topics: List<String> = emptyList(),
        val availableTopics: List<String> = emptyList(),
        val audioPath: String = "",
        val waveformData: List<Float> = emptyList(),
        val isRecording: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val isValid: Boolean
        get() = _uiState.value.run {
            title.isNotBlank() && audioPath.isNotBlank() && error == null
        }


    init {
        loadAvailableTopics()
    }

    private fun loadAvailableTopics() {
        viewModelScope.launch {
            topicRepository.getAllTopics()
                .collect { topics ->
                    _uiState.update { state ->
                        state.copy(
                            availableTopics = topics.filter { it !in state.topics }
                        )
                    }
                }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onMoodChange(newMood: Mood) {
        _uiState.update { it.copy(mood = newMood) }
    }

    fun onTopicAdd(topic: String)
    {
        _uiState.update { state ->
            state.copy(topics = state.topics + topic)
        }
    }

    fun onTopicRemove(topic: String)
    {
        _uiState.update { state ->
            state.copy(topics = state.topics - topic)
        }
    }

    fun startRecording() {
        viewModelScope.launch {
            try {
                val path = audioRecorder.startRecording()
                _uiState.update { it.copy(
                    audioPath = path,
                    isRecording = true,
                    error = null
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            audioRecorder.stopRecording()
            _uiState.update { it.copy(isRecording = false) }
        }
    }

    fun saveEntry(): Result<Unit> {
        return try {
            val currentState = _uiState.value
            if (!isValid) {
                throw IllegalStateException("Invalid entry state")
            }

            val entry = AudioEntry(
                id = UUID.randomUUID().toString(),
                timestamp = LocalDateTime.now(),
                audioPath = currentState.audioPath,
                title = currentState.title,
                description = currentState.description,
                mood = currentState.mood,
                topics = currentState.topics,
                duration = 0L, // TODO: Add actual duration
                waveformData = currentState.waveformData
            )

            viewModelScope.launch {
                audioRepository.insertEntry(entry)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorder.stopRecording()
    }
}