package com.sample.echojournal.di

import com.sample.echojournal.domain.repository.TopicRepository
import com.sample.echojournal.domain.usecase.topic.AddTopicUseCase
import com.sample.echojournal.domain.usecase.topic.DeleteTopicUseCase
import com.sample.echojournal.domain.usecase.topic.GetAllTopicsUseCase
import com.sample.echojournal.domain.usecase.topic.SearchTopicsUseCase
import com.sample.echojournal.domain.usecase.topic.TopicUseCases
import com.sample.echojournal.domain.usecase.topic.ValidateTopicUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideTopicUseCases(
        topicRepository: TopicRepository
    ): TopicUseCases {
        return TopicUseCases(
            addTopic = AddTopicUseCase(topicRepository, ValidateTopicUseCase()),
            searchTopics = SearchTopicsUseCase(topicRepository),
            getAllTopics = GetAllTopicsUseCase(topicRepository),
            deleteTopic = DeleteTopicUseCase(topicRepository),
            validateTopic = ValidateTopicUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideValidateTopicUseCase(): ValidateTopicUseCase
    {
        return ValidateTopicUseCase()
    }
}