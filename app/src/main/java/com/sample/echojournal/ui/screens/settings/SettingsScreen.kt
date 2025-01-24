package com.sample.echojournal.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.ui.components.DefaultTopicsSection
import com.sample.echojournal.ui.components.ErrorMessage
import com.sample.echojournal.ui.components.MoodSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DefaultMoodSection(
                selectedMood = uiState.defaultMood,
                onMoodSelected = viewModel::updateDefaultMood
            )

            DefaultTopicsSection (
                selectedTopics = uiState.defaultTopics,
                availableTopics = uiState.availableTopics,
                onTopicsUpdated = viewModel::updateDefaultTopics
            )

            uiState.error?.let { error ->
                ErrorMessage(
                    message = error,
                    onDismiss = viewModel::clearError
                )
            }
        }
    }
}

@Composable
private fun DefaultMoodSection(
    selectedMood: Mood,
    onMoodSelected: (Mood) -> Unit
) {
    Column {
        Text(
            text = "Default Mood",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        MoodSelector(
            selectedMood = selectedMood,
            onMoodSelected = onMoodSelected
        )
    }
}