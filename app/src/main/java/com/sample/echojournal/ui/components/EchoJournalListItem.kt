package com.sample.echojournal.ui.components

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sample.echojournal.domain.model.AudioEntry
import com.sample.echojournal.ui.model.JournalUi

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EchoJournalItem(
    journalUi: AudioEntry,
    onClick: () -> Unit,
    currentlyPlayingId: String?,
    onPlayAudio: (String) -> Unit,
    onStopAudio: () -> Unit
) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = journalUi.title.ifEmpty { "My Entry" },
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f).padding(end = 8.dp))

                Text(
                    text = journalUi.duration.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            AudioEntryCard(
                entry = journalUi,
                isPlaying = journalUi.id == currentlyPlayingId,
                onPlayClick = {
                    if (journalUi.id == currentlyPlayingId) {
                        onStopAudio()
                    } else {
                        onPlayAudio(journalUi.id)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                playButtonColor = journalUi.mood.color,
                bgColor = journalUi.mood.color,
                onTopicClick = { }
            )

            Spacer(Modifier.height(5.dp))

            if (!TextUtils.isEmpty(journalUi.description))
            {
                Text(
                    text = journalUi.description.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }


            TopicsHorizontalList(journalUi.topics)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EchoJournalListItem(journalUi: AudioEntry, isFirst: Boolean? = false, isLast: Boolean = false, onClick: () -> Unit, currentlyPlayingId: String?,
                        onPlayAudio: (String) -> Unit,
                        onStopAudio: () -> Unit)
{
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isFirst != null)
            {
                VerticalDivider(
                    modifier = Modifier.then(
                        if (isFirst)
                            Modifier.padding(top = 10.dp)
                        else if (isLast)
                            Modifier.height(10.dp)
                        else
                            Modifier
                    ),
                    thickness = 2.dp
                )
            }

            Image(
                painter = painterResource(journalUi.mood.iconResId),
                contentDescription = "Mood"
            )
        }

        EchoJournalItem(journalUi, onClick, currentlyPlayingId, onPlayAudio, onStopAudio)
    }

}