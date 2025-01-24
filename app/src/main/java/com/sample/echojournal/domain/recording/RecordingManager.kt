package com.sample.echojournal.domain.recording

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordingManager @Inject constructor(@ApplicationContext private val context: Context)
{
    private var recorder: MediaRecorder? = null
    private var currentFile: File? = null

    private val _recordingState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    val recordingState: StateFlow<RecordingState> = _recordingState

    private val _amplitude = MutableStateFlow(0f)
    val amplitude: StateFlow<Float> = _amplitude

    private var amplitudeJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val amplitudeBuffer = ArrayDeque<Float>(BUFFER_SIZE)

    @RequiresApi(Build.VERSION_CODES.O)
    fun startRecording(): String
    {
        val file = createAudioFile()
        currentFile = file

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        _recordingState.value = RecordingState.Recording
        startAmplitudeMonitoring()
        return file.absolutePath
    }

    fun pauseRecording()
    {
        recorder?.pause()
        _recordingState.value = RecordingState.Paused
    }

    fun resumeRecording()
    {
        recorder?.resume()
        _recordingState.value = RecordingState.Recording
    }

    fun stopRecording(): String? {
        return try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            _recordingState.value = RecordingState.Idle
            currentFile?.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun startAmplitudeMonitoring()
    {
        amplitudeJob = scope.launch {

            while (isActive && _recordingState.value == RecordingState.Recording)
            {
                try
                {
                    val amplitude = recorder?.maxAmplitude?.toFloat() ?: 0f
                    val normalizedAmplitude = normalizeAmplitude(amplitude)

                    amplitudeBuffer.apply {
                        if (size >= BUFFER_SIZE) removeFirst()
                        addLast(normalizedAmplitude)
                    }

                    _amplitude.value = amplitudeBuffer.average().toFloat()
                    delay(AMPLITUDE_UPDATE_INTERVAL)
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun normalizeAmplitude(amplitude: Float): Float
    {
        return (amplitude / MAX_AMPLITUDE).coerceIn(0f, 1f)
    }

    private fun stopAmplitudeMonitoring() {
        amplitudeJob?.cancel()
        amplitudeJob = null
        amplitudeBuffer.clear()
        _amplitude.value = 0f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAudioFile(): File {
        val timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        )
        return File(context.cacheDir, "audio_record_$timestamp.mp3")
    }


    companion object {
        private const val BUFFER_SIZE = 5
        private const val AMPLITUDE_UPDATE_INTERVAL = 100L
        private const val MAX_AMPLITUDE = 32767f
    }
}

sealed class RecordingState {
    object Idle : RecordingState()
    object Recording : RecordingState()
    object Paused : RecordingState()
}