package com.sample.echojournal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.sample.echojournal.ui.navigation.EchoJournalNavGraph
import com.sample.echojournal.ui.theme.EchoJournalTheme
import com.sample.echojournal.widget.RecordWidgetProvider.Companion.EXTRA_START_RECORDING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
        {
            handleRecordingStart()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        showSplashScreen()

        handleIntent(intent)

        setupContent()
    }

    private fun showSplashScreen()
    {
        val splashScreen = installSplashScreen()

        var keepSplashScreen = true

        // Set the condition to keep the splash screen on screen
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch {

            delay(2000) // Keeps splash screen for 2 seconds
            keepSplashScreen = false
        }
    }

    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?)
    {
        if (intent?.getBooleanExtra(EXTRA_START_RECORDING, false) == true)
        {
            checkPermissionAndRecord()
        }
    }

    private fun checkPermissionAndRecord()
    {
        when (checkSelfPermission(Manifest.permission.RECORD_AUDIO))
        {
            PackageManager.PERMISSION_GRANTED -> handleRecordingStart()

            else -> requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun handleRecordingStart()
    {
        setupContent(startRecording = true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupContent(startRecording: Boolean = false)
    {
        setContent {

            EchoJournalTheme {

                val navController = rememberNavController()

                var shouldStartRecording by remember { mutableStateOf(startRecording) }

                LaunchedEffect(shouldStartRecording) {
                    if (shouldStartRecording) {
                        navController.navigate("record")
                        shouldStartRecording = false
                    }
                }

                EchoJournalNavGraph(
                    navController = navController,
                    startRecording = { shouldStartRecording = true }
                )
            }
        }
    }
}