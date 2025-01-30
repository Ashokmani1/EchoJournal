package com.sample.echojournal.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.model.getMoodColor

@Composable
fun WaveformVisualizer(
    waveformData: List<Float>,
    isPlaying: Boolean,
    mood: Mood,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(25.dp)
    ) {
        val width = size.width
        val height = size.height
        val path = Path()
        
        val points = waveformData.mapIndexed { index, amplitude ->
            val x = width * index / (waveformData.size - 1)
            val y = height / 2 + (height / 2) * amplitude
            Offset(x, y)
        }

        // Draw background waveform
        points.forEachIndexed { index, point ->
            if (index == 0) {
                path.moveTo(point.x, point.y)
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        drawPath(
            path = path,
            color = mood.getMoodColor().copy(alpha = 0.3f),
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw progress overlay
        val progressPath = Path()
        val progressPoints = points.take((points.size * animatedProgress).toInt())
        
        progressPoints.forEachIndexed { index, point ->
            if (index == 0) {
                progressPath.moveTo(point.x, point.y)
            } else {
                progressPath.lineTo(point.x, point.y)
            }
        }

        drawPath(
            path = progressPath,
            color = mood.getMoodColor(),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}