package com.sample.echojournal.data.repository

import com.sample.echojournal.data.local.dao.TopicDao
import com.sample.echojournal.data.local.entity.TopicEntity
import com.sample.echojournal.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    private val topicDao: TopicDao
) : TopicRepository {
    override fun getAllTopics(): Flow<List<String>> =
        topicDao.getAllTopics().map { topics ->
            topics.map { it.name }
        }

    override fun searchTopics(query: String): Flow<List<String>> =
        topicDao.searchTopics(query).map { topics ->
            topics.map { it.name }
        }

    override suspend fun addTopic(name: String) {
        topicDao.insertTopic(TopicEntity(name = name))
    }

    override suspend fun deleteTopic(name: String) {
        topicDao.deleteTopic(TopicEntity(name = name))
    }

    override suspend fun getTopicByName(name: String): String? =
        topicDao.searchTopics(name)
            .map { topics -> topics.firstOrNull()?.name }
            .firstOrNull()
}