package com.sample.echojournal.domain.audio

import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AudioProcessor @Inject constructor()
{
    suspend fun processAudioFile(filePath: String): AudioMetadata = withContext(Dispatchers.IO)
    {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filePath)
        
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
        val waveform = generateWaveformData(filePath)
        
        retriever.release()
        
        AudioMetadata(duration, waveform)
    }

    private fun generateWaveformData(filePath: String): List<Float>
    {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        val samplesCount = 100 // Number of samples for visualization
        val amplitudes = mutableListOf<Float>()
        
        val recorder = MediaRecorder()
        try
        {
            for (i in 0 until samplesCount)
            {
                val amplitude = recorder.maxAmplitude.toFloat()
                amplitudes.add(normalizeAmplitude(amplitude))
            }
        }
        finally
        {
            recorder.release()
        }
        
        return amplitudes
    }

    private fun normalizeAmplitude(amplitude: Float): Float
    {
        val maxAmplitude = 32767f // Maximum possible amplitude
        return (amplitude / maxAmplitude).coerceIn(0f, 1f)
    }
}

data class AudioMetadata(
    val duration: Long,
    val waveformData: List<Float>
)