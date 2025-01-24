package com.sample.echojournal.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun RecordingFAB(
    isRecording: Boolean,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var isDragging by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(0f) }
    
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Delete indicator
        AnimatedVisibility(
            visible = isDragging,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Cancel Recording",
                modifier = Modifier
                    .offset(x = (-100).dp)
                    .size(32.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }

        // FAB with long press handling
        FloatingActionButton(
            onClick = onStartRecording,
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        onStartRecording()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount.x
                        if (dragOffset < -100f) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    },
                    onDragEnd = {
                        isDragging = false
                        if (dragOffset < -100f) {
                            onStopRecording()
                        }
                        dragOffset = 0f
                    },
                    onDragCancel = {
                        isDragging = false
                        dragOffset = 0f
                    }
                )
            }
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Check else Icons.Default.Mic,
                contentDescription = if (isRecording) "Stop Recording" else "Start Recording"
            )
        }
    }
}