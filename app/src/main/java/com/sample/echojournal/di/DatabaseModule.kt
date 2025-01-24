package com.sample.echojournal.di

import android.content.Context
import androidx.room.Room
import com.sample.echojournal.data.local.database.EchoJournalDatabase
import com.sample.echojournal.data.repository.AudioRepositoryImpl
import com.sample.echojournal.data.repository.SettingsRepositoryImpl
import com.sample.echojournal.data.repository.TopicRepositoryImpl
import com.sample.echojournal.domain.repository.AudioRepository
import com.sample.echojournal.domain.repository.SettingsRepository
import com.sample.echojournal.domain.repository.TopicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EchoJournalDatabase {
        return Room.databaseBuilder(
            context,
            EchoJournalDatabase::class.java,
            "echo_journal.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAudioEntryDao(database: EchoJournalDatabase) = database.audioEntryDao()

    @Provides
    @Singleton
    fun provideTopicDao(database: EchoJournalDatabase) = database.topicDao()

    @Provides
    @Singleton
    fun provideSettingsDao(database: EchoJournalDatabase) = database.settingsDao()

    @Provides
    @Singleton
    fun provideAudioRepository(repositoryImpl: AudioRepositoryImpl): AudioRepository = repositoryImpl

    @Provides
    @Singleton
    fun provideTopicRepository(repositoryImpl: TopicRepositoryImpl): TopicRepository = repositoryImpl

    @Provides
    @Singleton
    fun provideSettingsRepository(repositoryImpl: SettingsRepositoryImpl): SettingsRepository = repositoryImpl
}