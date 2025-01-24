package com.sample.echojournal.ui.screens.journal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.echojournal.R
import com.sample.echojournal.domain.audio.AudioRecorder
import com.sample.echojournal.ui.components.EntryList
import com.sample.echojournal.ui.components.FilterSection
import com.sample.echojournal.ui.components.RecordingBottomSheet
import com.sample.echojournal.ui.components.RecordingFAB
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalHistoryScreen(
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JournalHistoryViewModel = hiltViewModel(),
    audioRecorder: AudioRecorder
) {
    val uiState by viewModel.uiState.collectAsState()
    var showRecordingSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your EchoJournal") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            RecordingFAB(
                isRecording = uiState.isRecording,
                onStartRecording = { showRecordingSheet = true },
                onStopRecording = { viewModel.stopRecording() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            FilterSection(
                selectedMoods = uiState.selectedMoods,
                selectedTopics = uiState.selectedTopics,
                onMoodSelected = viewModel::updateMoodFilter,
                onTopicSelected = viewModel::updateTopicFilter
            )

            EntryList(
                entries = uiState.entries,
                currentlyPlayingId = uiState.currentlyPlayingId,
                onPlayAudio = viewModel::playAudio,
                onStopAudio = viewModel::stopAudio,
                onTopicClick = { }
            )

        }

        if (showRecordingSheet)
        {
            RecordingBottomSheet(
                onDismiss = { showRecordingSheet = false },
                onRecordingComplete = { audioPath -> 
                    viewModel.handleRecordingComplete(audioPath)
                    showRecordingSheet = false
                },
                audioRecorder = audioRecorder
            )
        }
    }
}

@Composable
private fun EmptyState()
{
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(painterResource(R.drawable.ic_journal_empty_img), contentDescription = null)

            Spacer(Modifier.height(20.dp))

            Text(
                text = "No Entries",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "Start recording your first Echo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}