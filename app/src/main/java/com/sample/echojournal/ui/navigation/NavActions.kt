package com.sample.echojournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun rememberEchoJournalNavigation(
    navController: NavHostController
): EchoJournalNavigationActions {
    return remember(navController) {
        EchoJournalNavigationActions(navController)
    }
}

class EchoJournalNavigationActions(private val navController: NavHostController)
{
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val navigateToJournalHistory: () -> Unit = {
        navController.navigate(NavRoute.JournalHistory.route) {

            launchSingleTop = true
        }
    }

    val navigateToRecord: () -> Unit = {
        navController.navigate(NavRoute.Record.route) {
            launchSingleTop = true
        }
    }

    val navigateToSettings: () -> Unit = {
        navController.navigate(NavRoute.Settings.route) {
            launchSingleTop = true
        }
    }
}