package com.sample.echojournal.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.sample.echojournal.domain.audio.AudioRecorder
import com.sample.echojournal.ui.navigation.NavRoute
import com.sample.echojournal.ui.navigation.NavRoute.Companion.createDeepLinkPattern
import com.sample.echojournal.ui.navigation.rememberEchoJournalNavigation
import com.sample.echojournal.ui.screens.splash.SplashScreen
import com.sample.echojournal.ui.screens.journal.JournalHistoryScreen
import com.sample.echojournal.ui.screens.record.CreateRecordScreen
import com.sample.echojournal.ui.screens.settings.SettingsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EchoJournalNavGraph(
    navController: NavHostController,
    startDestination: String = NavRoute.JournalHistory.route,
    startRecording: () -> Unit
) {
    val actions = rememberEchoJournalNavigation(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = NavRoute.JournalHistory.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            arguments = listOf(
                navArgument("filter") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = createDeepLinkPattern("journal/{filter}")
                }
            )
        ) { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter")
            JournalHistoryScreen(
                onNavigateToSettings = actions.navigateToSettings,
                audioRecorder = AudioRecorder(LocalContext.current)
//                onNavigateToRecord = actions.navigateToRecord,
//                startRecording = startRecording,
//                initialFilter = filter
            )

        }

        composable(NavRoute.Record.route) {
            CreateRecordScreen(
                onNavigateBack = actions.navigateUp
            )
        }

        composable(NavRoute.Settings.route) {
            SettingsScreen(
                onNavigateBack = actions.navigateUp
            )
        }
    }
}

