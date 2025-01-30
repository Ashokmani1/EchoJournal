package com.sample.echojournal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PrimaryButton( modifier: Modifier = Modifier, isEnabled: Boolean = true, onClick: () -> Unit, text: @Composable (isEnabled: Boolean) -> Unit)
{
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF578CFF),
            Color(0xFF1F70F5)
        )
    )

    Button(
        onClick = { onClick() },
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent),
        shape = CircleShape,
        modifier = modifier.then(if (isEnabled) Modifier.background(gradient, CircleShape) else Modifier.background(MaterialTheme.colorScheme.outlineVariant, CircleShape) )
    ) {
        text(isEnabled)
    }
}

@Composable
fun SecondaryButton(modifier: Modifier = Modifier, onClick: () -> Unit)
{
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent),
        shape = CircleShape,
        modifier = modifier.background(MaterialTheme.colorScheme.onPrimaryContainer, CircleShape)
    ) {
        Text(
            text = "Cancel",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary)
    }
}