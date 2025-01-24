package com.sample.echojournal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey val name: String,
    val createdAt: Long = System.currentTimeMillis()
)