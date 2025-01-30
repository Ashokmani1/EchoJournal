package com.sample.echojournal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AudioCard(playButtonColor: Color, bgColor: Color)
{
    Row (
        modifier = Modifier.fillMaxWidth().background(color = bgColor, shape = CircleShape).padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play button",
            modifier = Modifier.size(32.dp).background(color = MaterialTheme.colorScheme.onBackground, shape = CircleShape).padding(4.dp),
            tint = playButtonColor
        )

        Text(
            text = "0:00/12:30",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 5.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}