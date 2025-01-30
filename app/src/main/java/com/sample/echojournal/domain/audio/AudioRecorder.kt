package com.sample.echojournal.domain.audio

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AudioRecorder @Inject constructor(
    private val context: Context
) {
    private var recorder: MediaRecorder? = null
    private var currentFilePath: String? = null

    fun startRecording(): String
    {
        val fileName = createFileName()
        val file = File(context.filesDir, fileName)
        
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        
        currentFilePath = file.absolutePath
        return file.absolutePath
    }

    fun pauseRecording()
    {
        recorder?.pause()
    }

    fun resumeRecording()
    {
        recorder?.resume()
    }

    fun stopRecording(): String?
    {
        return try
        {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            currentFilePath
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            null
        }
    }

    private fun createFileName(): String
    {
        val timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        )
        return "audio_record_$timestamp.mp3"
    }
}