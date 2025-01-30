package com.sample.echojournal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sample.echojournal.R
import com.sample.echojournal.domain.model.Mood
import com.sample.echojournal.domain.model.getMoodIconResId
import com.sample.echojournal.ui.screens.journal.JournalListAction


@Composable
fun JournalFilterChip(
    value: String,
    onAction: (JournalListAction) -> Unit,
    onClear: () -> Unit,
    leadingIcons: List<Int>?,
    list: List<String>? = null,
    selectedIds: List<String> = emptyList()
) {
    var isFilterSelected by remember(selectedIds) { mutableStateOf(selectedIds.isNotEmpty()) }
    var expanded by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { expanded = true },
        label = {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge
            )
        },
        leadingIcon = {
            Row {
                leadingIcons?.forEachIndexed { index, it ->
                    Image(
                        painter = painterResource(it),
                        contentDescription = "Selected Mood",
                        modifier = Modifier
                            .size(22.dp)
                            .zIndex(index.toFloat())
                            .offset(x = (-index * 2).dp)
                    )
                }
            }
        },
        trailingIcon = {
            if (isFilterSelected)
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(18.dp).clip(CircleShape).clickable { onClear() }
                )
        },
        border = BorderStroke(1.dp, if (isFilterSelected || expanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outlineVariant),
        shape = CircleShape,
        colors = AssistChipDefaults.assistChipColors(containerColor = if (isFilterSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent)
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .size(width = screenWidth * 0.9f, height = screenHeight * 0.3f)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if(list != null)
        {
            list.forEach { item ->

                val isSelected = selectedIds.contains(item)

                DropdownMenuItem(
                    onClick = { if (isSelected) onAction(JournalListAction.DeselectTopic(item)) else onAction(JournalListAction.SelectTopic(item)) },
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "#",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(4.dp).alpha(0.5f))
                    },
                    trailingIcon = {
                        if (selectedIds.contains(item))
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_selected_tick),
                                contentDescription = item,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .then(
                            if (selectedIds.contains(item))
                                Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            else
                                Modifier
                        )
                )
            }
        }
        else
        {

            Mood.entries.forEach { mood ->

                val isSelected = selectedIds.contains(mood.name)

                DropdownMenuItem(
                    onClick = { if (isSelected) onAction(JournalListAction.DeselectMood(mood)) else onAction(JournalListAction.SelectMood(mood)) },
                    text = {
                        Text(
                            text = mood.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary)
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = mood.getMoodIconResId()),
                            contentDescription = mood.name,
                            modifier = Modifier.size(24.dp))
                    },
                    trailingIcon = {
                        if (selectedIds.contains(mood.name))
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_selected_tick),
                                contentDescription = mood.name,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .then(
                            if (selectedIds.contains(mood.name))
                                Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            else
                                Modifier
                        )
                )
            }
        }
    }
}

@Composable
fun TopicChip(topic: String, onClear: (() -> Unit)? = null)
{
    Row(
        modifier = Modifier
            .background(Color(0xFFF2F2F7), CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 4.dp).alpha(0.5f))

        Text(
            text = topic,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        if (onClear != null)
        {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Clear",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(2.dp).size(16.dp).clip(CircleShape).clickable { onClear() }.alpha(0.3f))
        }
    }
}