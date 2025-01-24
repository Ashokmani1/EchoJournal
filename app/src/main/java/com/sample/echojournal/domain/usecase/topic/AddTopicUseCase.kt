package com.sample.echojournal.domain.usecase.topic

import com.sample.echojournal.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class TopicUseCases(
    val addTopic: AddTopicUseCase,
    val searchTopics: SearchTopicsUseCase,
    val getAllTopics: GetAllTopicsUseCase,
    val deleteTopic: DeleteTopicUseCase,
    val validateTopic: ValidateTopicUseCase
)

class AddTopicUseCase @Inject constructor(
    private val repository: TopicRepository,
    private val validateTopic: ValidateTopicUseCase
) {
    suspend operator fun invoke(name: String): Result<Unit> {
        return try {
            validateTopic(name).getOrThrow()
            repository.addTopic(name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SearchTopicsUseCase @Inject constructor(
    private val repository: TopicRepository
) {
    operator fun invoke(query: String): Flow<List<String>> =
        repository.searchTopics(query)
}

class GetAllTopicsUseCase @Inject constructor(
    private val repository: TopicRepository
) {
    operator fun invoke(): Flow<List<String>> =
        repository.getAllTopics()
}

class DeleteTopicUseCase @Inject constructor(
    private val repository: TopicRepository
) {
    suspend operator fun invoke(name: String): Result<Unit> {
        return try {
            repository.deleteTopic(name)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ValidateTopicUseCase {
    operator fun invoke(name: String): Result<Unit> {
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Topic name cannot be empty"))
        }
        if (name.length < 2) {
            return Result.failure(IllegalArgumentException("Topic name must be at least 2 characters"))
        }
        if (name.length > 50) {
            return Result.failure(IllegalArgumentException("Topic name must not exceed 50 characters"))
        }
        return Result.success(Unit)
    }
}