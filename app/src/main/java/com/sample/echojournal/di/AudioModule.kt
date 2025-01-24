package com.sample.echojournal.di

import android.content.Context
import com.sample.echojournal.domain.audio.AudioPlayer
import com.sample.echojournal.domain.audio.AudioProcessor
import com.sample.echojournal.domain.audio.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioModule {

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: Context
    ): AudioPlayer {
        return AudioPlayer(context)
    }

    @Provides
    @Singleton
    fun provideAudioProcessor(): AudioProcessor {
        return AudioProcessor()
    }

    @Provides
    @Singleton
    fun provideAudioRecorder(
        @ApplicationContext context: Context
    ): AudioRecorder {
        return AudioRecorder(context)
    }
}