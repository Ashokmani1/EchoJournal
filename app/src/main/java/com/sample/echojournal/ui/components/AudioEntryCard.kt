package com.sample.echojournal.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sample.echojournal.domain.model.AudioEntry
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AudioEntryCard(
    entry: AudioEntry,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    playButtonColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HHmm") }

    Column {

        Row (
            modifier = Modifier.fillMaxWidth().background(color = bgColor, shape = CircleShape).padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else  Icons.Default.PlayArrow,
                contentDescription = "Play button",
                modifier = Modifier.size(32.dp).background(color = MaterialTheme.colorScheme.onBackground, shape = CircleShape).clickable {  onPlayClick.invoke() }.padding(4.dp),
                tint = playButtonColor
            )

            Box(modifier = Modifier.weight(1f).padding(horizontal = 5.dp)) {

                WaveformVisualizer(
                    waveformData = entry.waveformData,
                    isPlaying = isPlaying,
                    mood = entry.mood
                )
            }


            Text(
                text = entry.timestamp.format(timeFormatter),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 5.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (entry.description != null) {
            Text(
                text = entry.description,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            if (entry.description.lines().size > 3) {
                TextButton(onClick = { isExpanded = !isExpanded }) {
                    Text(if (isExpanded) "Show less" else "Show more")
                }
            }
        }

        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            entry.topics.forEach { topic ->
                AssistChip(
                    onClick = { onTopicClick(topic) },
                    label = { Text(topic) }
                )
            }
        }
    }
}