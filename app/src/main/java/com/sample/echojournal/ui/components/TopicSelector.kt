package com.sample.echojournal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TopicSelector(
    selectedTopics: List<String>,
    availableTopics: List<String>,
    onTopicAdd: (String) -> Unit,
    onTopicRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTopicDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Topics",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedTopics.forEach { topic ->
                AssistChip(
                    onClick = { onTopicRemove(topic) },
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

    if (showTopicDialog)
    {
        TopicSelectionDialog(
            availableTopics = availableTopics.filter { it !in selectedTopics },
            onTopicSelected = { topic ->
                onTopicAdd(topic)
                showTopicDialog = false
            },
            onDismiss = { showTopicDialog = false }
        )
    }
}


@Composable
private fun TopicSelectionDialog(
    availableTopics: List<String>,
    onTopicSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Topic") },
        text = {
            LazyColumn {
                items(availableTopics) { topic ->
                    Text(
                        text = topic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTopicSelected(topic) }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}