package com.sample.echojournal.data.mapper

import com.sample.echojournal.data.local.entity.AudioEntryEntity
import com.sample.echojournal.domain.model.AudioEntry
import com.sample.echojournal.domain.model.Mood

fun AudioEntryEntity.toAudioEntry() = AudioEntry(
    id = id,
    timestamp = timestamp,
    audioPath = audioPath,
    title = title,
    description = description,
    mood = Mood.valueOf(mood),
    topics = topics,
    duration = duration,
    waveformData = waveformData
)

fun AudioEntry.toAudioEntryEntity() = AudioEntryEntity(
    id = id,
    timestamp = timestamp,
    audioPath = audioPath,
    title = title,
    description = description,
    mood = mood.name,
    topics = topics,
    duration = duration,
    waveformData = waveformData
)