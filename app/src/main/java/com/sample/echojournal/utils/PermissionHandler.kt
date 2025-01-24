package com.sample.echojournal.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionHandler @Inject constructor(
    private val context: Context
) {
    fun hasRecordAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
    }
}