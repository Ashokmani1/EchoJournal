package com.sample.echojournal.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.repository.SettingsRepository
import com.sample.echojournal.domain.usecase.topic.TopicUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val topicUseCases: TopicUseCases
) : ViewModel() {

    data class SettingsUiState(
        val defaultMood: Mood = Mood.Neutral,
        val defaultTopics: List<String> = emptyList(),
        val availableTopics: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        loadAvailableTopics()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.getDefaultMood(),
                settingsRepository.getDefaultTopics()
            ) { mood, topics ->
                _uiState.update { state ->
                    state.copy(
                        defaultMood = mood ?: Mood.Neutral,
                        defaultTopics = topics
                    )
                }
            }.collect()
        }
    }

    private fun loadAvailableTopics() {
        viewModelScope.launch {
            topicUseCases.getAllTopics()
                .collect { topics ->
                    _uiState.update { it.copy(availableTopics = topics) }
                }
        }
    }

    fun updateDefaultMood(mood: Mood) {
        viewModelScope.launch {
            try {
                settingsRepository.updateDefaultMood(mood)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateDefaultTopics(topics: List<String>) {
        viewModelScope.launch {
            try {
                settingsRepository.updateDefaultTopics(topics)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}