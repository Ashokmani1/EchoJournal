package com.sample.echojournal.domain.audio

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPath: String? = null

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    fun play(audioPath: String) {
        if (currentPath == audioPath && mediaPlayer?.isPlaying == true) {
            pause()
            return
        }

        if (currentPath != audioPath) {
            reset()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioPath)
                prepare()
                setOnCompletionListener { 
                    _playbackState.value = PlaybackState.Completed
                }
                setOnErrorListener { _, _, _ -> 
                    _playbackState.value = PlaybackState.Error
                    true
                }
            }
            currentPath = audioPath
        }

        mediaPlayer?.start()
        _playbackState.value = PlaybackState.Playing
    }

    fun pause() {
        mediaPlayer?.pause()
        _playbackState.value = PlaybackState.Paused
    }

    fun stop() {
        reset()
        _playbackState.value = PlaybackState.Idle
    }

    private fun reset()
    {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }
        mediaPlayer = null
        currentPath = null
        _progress.value = 0f
    }

    fun setVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    private fun startProgressTracking()
    {
        mediaPlayer?.let { player ->
            _progress.value = player.currentPosition.toFloat() / player.duration
        }
    }
}
