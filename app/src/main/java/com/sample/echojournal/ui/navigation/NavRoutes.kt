package com.sample.echojournal.ui.navigation

sealed class NavRoute(val route: String)
{
    object JournalHistory : NavRoute("journal_history")
    object Record : NavRoute("record")
    object Settings : NavRoute("settings")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    companion object {
        const val DEEP_LINK_SCHEME = "echojournal"
        const val DEEP_LINK_HOST = "app"

        fun createDeepLinkPattern(route: String) =
            "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/$route"
    }
}