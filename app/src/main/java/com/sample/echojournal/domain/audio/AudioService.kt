package com.sample.echojournal.domain.audio

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentAudioPath: String? = null

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    fun playAudio(audioPath: String) {
        if (currentAudioPath == audioPath && mediaPlayer?.isPlaying == true) {
            pauseAudio()
            return
        }

        if (currentAudioPath != audioPath) {
            stopAudio()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioPath)
                prepare()
                setOnCompletionListener {
                    _playbackState.value = PlaybackState.Completed
                    _progress.value = 0f
                }
            }
            currentAudioPath = audioPath
        }

        mediaPlayer?.start()
        _playbackState.value = PlaybackState.Playing
    }

    fun pauseAudio() {
        mediaPlayer?.pause()
        _playbackState.value = PlaybackState.Paused
    }

    fun stopAudio() {
        mediaPlayer?.apply {
            stop()
            reset()
            release()
        }
        mediaPlayer = null
        currentAudioPath = null
        _playbackState.value = PlaybackState.Idle
        _progress.value = 0f
    }

    fun seekTo(position: Float) {
        mediaPlayer?.let { player ->
            val seekPosition = (position * player.duration).toInt()
            player.seekTo(seekPosition)
            _progress.value = position
        }
    }
}

sealed class PlaybackState
{
    object Idle : PlaybackState()
    object Playing : PlaybackState()
    object Paused : PlaybackState()
    object Error : PlaybackState()
    object Completed : PlaybackState()
}