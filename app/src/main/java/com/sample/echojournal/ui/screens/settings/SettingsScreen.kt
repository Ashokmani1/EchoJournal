package com.sample.echojournal.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.echojournal.R
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.ui.components.DefaultTopicsSection
import com.sample.echojournal.ui.components.ErrorMessage
import com.sample.echojournal.ui.components.MoodSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val bgGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.inverseOnSurface,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )

    Scaffold(
        modifier = Modifier.background(brush = bgGradient),
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(painter = painterResource(R.drawable.ej_ic_back), "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            DefaultMoodSection(
                selectedMood = uiState.defaultMood,
                onMoodSelected = viewModel::updateDefaultMood
            )

            DefaultTopicsSection (
                selectedTopics = uiState.defaultTopics,
                availableTopics = uiState.availableTopics,
                onTopicsUpdated = viewModel::updateDefaultTopics
            )

            uiState.error?.let { error ->
                ErrorMessage(
                    message = error,
                    onDismiss = viewModel::clearError
                )
            }
        }
    }
}

@Composable
private fun DefaultMoodSection(
    selectedMood: Mood,
    onMoodSelected: (Mood) -> Unit
) {
    Card(colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.onPrimary
    )) {

        Column(modifier = Modifier.padding(10.dp)) {

            Text(
                text = "My Mood",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(1.dp))

            Text(
                text = "Select default mood to apply to all new entries",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(15.dp))

            MoodSelector(
                selectedMood = selectedMood,
                onMoodSelected = onMoodSelected
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettingsScreen()
{
    DefaultMoodSection(Mood.NEUTRAL, {})
}