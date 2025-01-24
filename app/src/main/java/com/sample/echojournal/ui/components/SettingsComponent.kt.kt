package com.sample.echojournal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.echojournal.domain.model.Mood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSelector(
    selectedMood: Mood,
    onMoodSelected: (Mood) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Mood.entries.forEach { mood ->
            FilterChip(
                selected = mood == selectedMood,
                onClick = { onMoodSelected(mood) },
                label = { Text(mood.name) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = mood.color.copy(alpha = 0.2f)
                )
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefaultTopicsSection(
    selectedTopics: List<String>,
    availableTopics: List<String>,
    onTopicsUpdated: (List<String>) -> Unit
) {
    var showTopicDialog by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Default Topics",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedTopics.forEach { topic ->
                AssistChip(
                    onClick = { /* Remove topic */ },
                    label = { Text(topic) },
                    trailingIcon = {
                        Icon(Icons.Default.Close, "Remove topic")
                    }
                )
            }
            FilledTonalButton(
                onClick = { showTopicDialog = true }
            ) {
                Icon(Icons.Default.Add, "Add topic")
                Spacer(Modifier.width(4.dp))
                Text("Add Topic")
            }
        }
    }

    if (showTopicDialog) {
        TopicSelectionDialog(
            availableTopics = availableTopics,
            selectedTopics = selectedTopics,
            onTopicsSelected = onTopicsUpdated,
            onDismiss = { showTopicDialog = false }
        )
    }
}

@Composable
private fun TopicSelectionDialog(
    availableTopics: List<String>,
    selectedTopics: List<String>,
    onTopicsSelected: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableStateOf(selectedTopics.toSet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Default Topics") },
        text = {
            LazyColumn {
                items(availableTopics) { topic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selected = if (topic in selected) {
                                    selected - topic
                                } else {
                                    selected + topic
                                }
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(topic)
                        Checkbox(
                            checked = topic in selected,
                            onCheckedChange = { checked ->
                                selected = if (checked) {
                                    selected + topic
                                } else {
                                    selected - topic
                                }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTopicsSelected(selected.toList())
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        action = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    ) {
        Text(message)
    }
}