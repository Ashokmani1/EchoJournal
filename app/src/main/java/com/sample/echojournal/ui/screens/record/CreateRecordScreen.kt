package com.sample.echojournal.ui.screens.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.echojournal.R
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.ui.components.AudioPreview
import com.sample.echojournal.ui.components.MoodSelector
import com.sample.echojournal.ui.components.TopicSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecordScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateRecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler { showExitDialog = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Entry", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(painter = painterResource(R.drawable.ej_ic_back), "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            MoodSelector(
                selectedMood = uiState.mood,
                onMoodSelected = viewModel::onMoodChange
            )

            TopicSelector(
                selectedTopics = uiState.topics,
                availableTopics = uiState.availableTopics,
                onTopicAdd = viewModel::onTopicAdd,
                onTopicRemove = viewModel::onTopicRemove,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 3
            )

            AudioPreview(
                mood = uiState.mood,
                waveformData = uiState.waveformData
            )

            Button(
                onClick = viewModel::saveEntry,
                enabled = viewModel.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Discard Changes?") },
            text = { Text("Are you sure you want to discard this recording?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}