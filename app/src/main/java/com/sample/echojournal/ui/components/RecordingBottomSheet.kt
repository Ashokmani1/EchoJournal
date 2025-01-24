package com.sample.echojournal.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.time.Duration
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.accompanist.permissions.isGranted
import com.sample.echojournal.domain.audio.AudioRecorder
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RecordingBottomSheet(
    onDismiss: () -> Unit,
    onRecordingComplete: (String) -> Unit,
    audioRecorder: AudioRecorder,
    modifier: Modifier = Modifier
) {
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    var isRecording by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableStateOf(Duration.ZERO) }
    var recordingPath by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isRecording) {
        if (isRecording && !isPaused) {
            while (true) {
                delay(1000)
                recordingDuration = recordingDuration.plusSeconds(1)
            }
        }
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            try {
                recordingPath = audioRecorder.startRecording()
            } catch (e: Exception) {
                // Handle recording error
                isRecording = false
                recordingPath = null
            }
        } else {
            recordingPath?.let { audioRecorder.stopRecording() }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDuration(recordingDuration),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Recording visualization
            RecordingVisualizer(
                isRecording = isRecording && !isPaused,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Control buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Close, "Cancel Recording")
                }

                FloatingActionButton(
                    onClick = {
                        if (!isRecording) {
                            if (permissionState.status.isGranted)
                            {
                                isRecording = true
                            }
                            else
                            {
                                permissionState.launchPermissionRequest()
                            }
                        } else {
                            isPaused = !isPaused
                        }
                    }
                ) {
                    Icon(
                        imageVector = when {
                            !isRecording -> Icons.Default.Mic
                            isPaused -> Icons.Default.PlayArrow
                            else -> Icons.Default.Pause
                        },
                        contentDescription = when {
                            !isRecording -> "Start Recording"
                            isPaused -> "Resume Recording"
                            else -> "Pause Recording"
                        }
                    )
                }

                IconButton(
                    onClick = {
                        if (isRecording) {
                            recordingPath?.let { path ->
                                onRecordingComplete(path)
                                onDismiss()
                            }
                        }
                    },
                    enabled = isRecording,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Check, "Complete Recording")
                }
            }
        }
    }
}

private fun formatDuration(duration: Duration): String
{
    val minutes = duration.toMinutes()
    val seconds = duration.seconds % 60
    return "%02d:%02d".format(minutes, seconds)
}