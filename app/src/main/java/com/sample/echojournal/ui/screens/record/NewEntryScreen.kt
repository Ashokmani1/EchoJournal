package com.sample.echojournal.ui.screens.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.echojournal.ui.components.AudioCard
import com.sample.echojournal.ui.components.PrimaryButton
import com.sample.echojournal.ui.components.SecondaryButton
import com.sample.echojournal.ui.theme.EchoJournalTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(onNavigateBack: () -> Unit,
                       modifier: Modifier = Modifier,
                       viewModel: CreateRecordViewModel = hiltViewModel())
{
    var text by remember { mutableStateOf("My Entry") }

    var description by remember { mutableStateOf("") }


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler { showExitDialog = true }

    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "New Entry",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        },
        bottomBar = {
            Row(
               modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(3f)
                )

                PrimaryButton(
                    onClick = viewModel::saveEntry ,
                    modifier = Modifier.weight(7f)
                ) { isEnabled ->

                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline)
                }


            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Mood",
                    modifier = Modifier.padding(end = 5.dp).background(MaterialTheme.colorScheme.inverseOnSurface, CircleShape).padding(6.dp).size(22.dp),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )

                BasicTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    decorationBox = {

                        if(uiState.title.isEmpty())
                        {
                            Text(
                                text = "Add Title...",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }

                        it()
                    }
                )
            }

            AudioCard(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inverseOnSurface)

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "#",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )

                BasicTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        if (uiState.description.isEmpty())
                        {
                            Text(
                                text = "Topic",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outlineVariant)
                        }

                        it()
                    }
                )
            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Add Mood",
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(20.dp))

                BasicTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        if (uiState.description.isEmpty())
                        {
                            Text(
                                text = "Add Description...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outlineVariant)
                        }

                        it()
                    }
                )
            }

        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Discard Changes?") },
            text = { Text("Are you sure you want to discard this recording?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}