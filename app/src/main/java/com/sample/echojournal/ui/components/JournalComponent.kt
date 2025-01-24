package com.sample.echojournal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.echojournal.R
import com.sample.echojournal.domain.model.Mood

@Composable
fun FilterSection(
    selectedMoods: Set<Mood>,
    selectedTopics: Set<String>,
    onMoodSelected: (Mood) -> Unit,
    onTopicSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, top = 0.dp, start = 16.dp, end = 16.dp)
    ) {
        
        MoodFilterChips(
            selectedMoods = selectedMoods,
            onMoodSelected = onMoodSelected
        )

        Spacer(modifier = Modifier.height(8.dp))

        TopicFilterChips(
            selectedTopics = selectedTopics,
            onTopicSelected = onTopicSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodFilterChips(
    selectedMoods: Set<Mood>,
    onMoodSelected: (Mood) -> Unit
) {
    val displayMoods = remember(selectedMoods) {

        when
        {
            selectedMoods.isEmpty() -> "All Moods"

            selectedMoods.size > 2 -> "${selectedMoods.take(2).joinToString(", ")} +${selectedMoods.size - 2}"

            else -> selectedMoods.joinToString(", ")
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(arrayOf(displayMoods, "All Topics")) { mood ->

            FilterChipEachRow(mood, selectedMoods) { selectedMood ->

                onMoodSelected(selectedMood)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipEachRow(chip: String,  selectedMoods: Set<Mood>, onChipState: (Mood) -> Unit)
{
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(end = 7.dp, bottom = 6.dp)) {

        Surface(
            shape = MaterialTheme.shapes.large,
            color = if (selectedMoods.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Transparent,
            border = if (expanded) BorderStroke(1.dp, color = MaterialTheme.colorScheme.primaryContainer) else BorderStroke(1.dp, color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.clickable { 
                expanded = !expanded
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp)) {
                Text(
                    chip,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(min = 350.dp).background(MaterialTheme.colorScheme.onPrimary),

        ) {
            Mood.entries.forEach { mood ->

                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onChipState(mood)
                    },

                    text =
                    {
                        Text(mood.name)
                    },

                    leadingIcon = {
                        Image(
                            painter = painterResource(id = mood.iconResId),
                            contentDescription = mood.name,
                            modifier = Modifier.size(24.dp)
                        )
                    },

                    trailingIcon = {
                        if (selectedMoods.contains(mood)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_tick_icon),
                                contentDescription = mood.name,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    },

                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 1.dp),

                    modifier = if (selectedMoods.contains(mood)) {
                        Modifier.padding(3.dp).background(Color.Gray, shape = RoundedCornerShape(10.dp))
                    } else {
                        Modifier
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicFilterChips(
    selectedTopics: Set<String>,
    onTopicSelected: (String) -> Unit
) {
    val displayTopics = remember(selectedTopics) {
        if (selectedTopics.size > 2) {
            "${selectedTopics.take(2).joinToString(", ")} +${selectedTopics.size - 2}"
        } else {
            selectedTopics.joinToString(", ")
        }
    }

    FilterChip(
        selected = selectedTopics.isNotEmpty(),
        onClick = { /* Show topic selection dialog */ },
        label = { Text(displayTopics.ifEmpty { "Select Topics" }) }
    )
}
