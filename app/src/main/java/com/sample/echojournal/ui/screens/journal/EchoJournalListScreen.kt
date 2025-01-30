package com.sample.echojournal.ui.screens.journal

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sample.echojournal.R
import com.sample.echojournal.domain.audio.AudioRecorder
import com.sample.echojournal.ui.components.EchoJournalListItem
import com.sample.echojournal.ui.components.JournalFilterChip
import com.sample.echojournal.ui.components.RecordingBottomSheet
import com.sample.echojournal.ui.components.RecordingFAB
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EchoJournalListScreenRoot(audioRecorder: AudioRecorder,     onNavigateToSettings: () -> Unit,
                              onNavigateToNewEntry: () -> Unit, viewModel: JournalHistoryViewModel = hiltViewModel())
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    JournalHistoryScreen(uiState, audioRecorder, onNavigateToSettings, onNavigateToNewEntry, viewModel::onAction)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalFoundationApi::class)
@Composable
fun JournalHistoryScreen(
    uiState: JournalHistoryViewModel.JournalHistoryUiState,
    audioRecorder: AudioRecorder,
    onNavigateToSettings: () -> Unit,
    onNavigateToNewEntry: () -> Unit,
    onAction: (JournalListAction) -> Unit
) {

    var showRecordingSheet by remember { mutableStateOf(false) }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF578CFF),
            Color(0xFF0057CC)
        )
    )

    val bgGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.inverseOnSurface,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )

    var isRecording by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Scaffold(
        modifier = Modifier.background(brush = bgGradient),
        containerColor = Color.Transparent,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Echo Journal",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { onNavigateToSettings.invoke() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
                )

                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val (displayingMoods, moodIcons: List<Int>?) = remember(uiState.selectedMoods) {
                        uiState.selectedMoods.let {
                            when
                            {
                                it.isEmpty() -> Pair("All Moods", null)

                                it.size > 2 -> Pair("${it.take(2).joinToString(", ")} +${it.size - 2}", it.take(2).map { it.iconResId })

                                else -> Pair(it.joinToString(", "), it.map { it.iconResId }.toList())
                            }
                        }
                    }

                    val displayTopics = remember(uiState.selectedTopics) {
                        uiState.selectedTopics.let {
                            when
                            {
                                it.isEmpty() -> "All Topics"

                                it.size > 2 -> "${it.take(2).joinToString(", ")} +${it.size - 2}"

                                else -> it.joinToString(", ")
                            }
                        }
                    }

                    JournalFilterChip(
                        value = displayingMoods,
                        onSelected = onAction,
                        onClear = { onAction(JournalListAction.ClearMoodFilter) },
                        isSelected = false,
                        leadingIcons = moodIcons)

                    JournalFilterChip(
                        value = displayTopics,
                        onSelected = onAction,
                        onClear = { onAction(JournalListAction.ClearTopicFilter) },
                        isSelected = false,
                        leadingIcons = null,
                        list = listOf("a","work","personal", "private", "public"),
                        selectedIds = listOf("a","work","personal")
                    )
                }
            }
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(50.dp),
                horizontalAlignment = Alignment.End
            ) {

                if (uiState.isRecording)
                {
                    IconButton(
                        onClick = {
                            onAction(JournalListAction.RecordingComplete(""))
                                  },
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_selected_tick),
                            contentDescription = "Done",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp)
                        )
                    }

                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp)
                ) {

                    if (uiState.isRecording)
                    {
                        IconButton(
                            onClick = {
                                onAction(JournalListAction.DiscardRecording)
                                      },
                            modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = CircleShape)) {
                            Icon(painter = painterResource(R.drawable.ic_discard),
                                contentDescription = "Discard",
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(48.dp).padding(8.dp))
                        }
                    }

                    Box(modifier = Modifier) {
                        if (uiState.isRecording)
                        {
                            repeat(2) { index ->
                                Box(modifier = Modifier.matchParentSize().scale(scale)
                                    .scale(1f + (index * 0.2f)).alpha(0.3f - (index * 0.1f))
                                    .background(brush = gradient, shape = CircleShape))
                            }
                        }

                        var offsetX by remember { mutableStateOf(0f) }

                        var recording by remember { mutableStateOf(false) }

                        Icon(painter = painterResource(
                            if (uiState.isRecording) R.drawable.ic_mic else R.drawable.ic_add_record),
                            contentDescription = "Record", tint = Color.White,
                            modifier = Modifier.background(brush = gradient, shape = CircleShape)
                                .size(64.dp)
                                .padding(16.dp)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures(onDragStart = {
                                        offsetX = 0f
                                    }) { change, dragAmount ->
                                        if (recording)
                                        {
                                            change.consume()
                                            offsetX += dragAmount
                                            if (offsetX <= -50.dp.toPx())
                                            {
                                                onAction(JournalListAction.DiscardRecording)
                                            }
                                        }
                                    }
                                }
                                .combinedClickable(onClick = {
                                    if (!uiState.isRecording)
                                    {
                                        onAction(JournalListAction.CanShowSheetToRecord(true))
                                    }
                                }, onLongClick = {
                                    if (!uiState.isRecording)
                                    {
                                        onAction(JournalListAction.StartRecording)
                                        recording = true
                                    }
                                })
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Column(
            Modifier.padding(innerPadding)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
            ) {

                if(uiState.entries.isEmpty())
                {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(painterResource(R.drawable.ic_journal_empty_img), contentDescription = "No Entries")

                        Spacer(Modifier.height(16.dp))

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

                        Spacer(Modifier.height(120.dp))
                    }
                }
                else
                {
                    val grouped =  uiState.entries.groupBy { entry ->
                        entry.timestamp.toLocalDate()
                    }

                    LazyColumn {

                        grouped.forEach { (date, entries) ->

                            val displayText = when {
                                date == LocalDate.now()              -> "Today"
                                date == LocalDate.now().minusDays(1) -> "Yesterday"
                                else                                 -> date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
                            }

                            item {

                                Text(
                                    text = displayText,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 10.dp))
                            }


                            itemsIndexed(entries) { index, item ->

                                EchoJournalListItem(
                                    journalUi = item,
                                    isFirst = entries.size == 1 || index == 0,
                                    isLast = index == entries.size - 1,
                                    onClick = { onNavigateToNewEntry.invoke() },
                                    currentlyPlayingId = uiState.currentlyPlayingId,
                                    onPlayAudio = { onAction(JournalListAction.PlayAudio(item.id)) },
                                    onStopAudio = { onAction(JournalListAction.StopAudio) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showRecordingSheet)
        {
            RecordingBottomSheet(
                onDismiss = { showRecordingSheet = false },
                onRecordingComplete = { audioPath ->
                    onAction(JournalListAction.RecordingComplete(audioPath))
                    showRecordingSheet = false
                },
                audioRecorder = audioRecorder
            )
        }
    }
}
