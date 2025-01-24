package com.sample.echojournal.domain.usecase

import com.sample.echojournal.domain.audio.AudioRecorder
import javax.inject.Inject

class RecordAudioUseCase @Inject constructor(
    private val audioRecorder: AudioRecorder
) {
    fun startRecording(): String = audioRecorder.startRecording()
    
    fun pauseRecording() = audioRecorder.pauseRecording()
    
    fun resumeRecording() = audioRecorder.resumeRecording()
    
    fun stopRecording(): String? = audioRecorder.stopRecording()
}