package com.sample.echojournal.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sample.echojournal.R
import com.sample.echojournal.domain.model.AudioEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EntryList(
    entries: List<AudioEntry>,
    currentlyPlayingId: String?,
    onPlayAudio: (String) -> Unit,
    onStopAudio: () -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val grouped = entries.groupBy { entry ->
                entry.timestamp.toLocalDate()
            }

            grouped.forEach { (date, groupEntries) ->
                item(key = "header_${date}") {
                    DateHeader(date = date)
                }

                items(
                    items = groupEntries,
                    key = { it.id }
                ) { entry ->
                    AudioEntryCard(
                        entry = entry,
                        isPlaying = entry.id == currentlyPlayingId,
                        onPlayClick = {
                            if (entry.id == currentlyPlayingId) {
                                onStopAudio()
                            } else {
                                onPlayAudio(entry.id)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),

                        onTopicClick = onTopicClick,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateHeader(date: LocalDate)
{
    val displayText = when {
        date == LocalDate.now() -> "Today"
        date == LocalDate.now().minusDays(1) -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
    }

    Text(
        text = displayText,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
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