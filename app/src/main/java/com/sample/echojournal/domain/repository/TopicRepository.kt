package com.sample.echojournal.domain.repository

import kotlinx.coroutines.flow.Flow

interface TopicRepository {
    fun getAllTopics(): Flow<List<String>>
    fun searchTopics(query: String): Flow<List<String>>
    suspend fun addTopic(name: String)
    suspend fun deleteTopic(name: String)
    suspend fun getTopicByName(name: String): String?
}