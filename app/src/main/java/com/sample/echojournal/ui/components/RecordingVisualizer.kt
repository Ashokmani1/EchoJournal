package com.sample.echojournal.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun RecordingVisualizer(
    isRecording: Boolean,
    modifier: Modifier = Modifier
) {
    val bars = 30
    val animatedValues = remember { List(bars) { Animatable(0f) } }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (true) {
                animatedValues.forEach { animatable ->
                    launch {
                        animatable.animateTo(
                            targetValue = Random.nextFloat(),
                            animationSpec = tween(
                                durationMillis = 100,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
                delay(100)
            }
        }
    }

    val colors = animatedValues.map { animatable ->
        MaterialTheme.colorScheme.primary.copy(
            alpha = 0.3f + (animatable.value * 0.7f)
        )
    }

    Canvas(modifier = modifier) {
        val barWidth = size.width / (2 * bars - 1)
        val maxHeight = size.height

        animatedValues.forEachIndexed { index, animatable ->

            val height = maxHeight * animatable.value
            val x = index * (barWidth * 2)
            val color = colors[index]

            drawLine(
                color = color,
                start = Offset(x, maxHeight / 2 + height / 2),
                end = Offset(x, maxHeight / 2 - height / 2),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
}