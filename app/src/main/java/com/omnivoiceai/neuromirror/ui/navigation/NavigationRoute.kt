package com.omnivoiceai.neuromirror.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable data object SplashScreen: NavigationRoute
    @Serializable data object HomeScreen: NavigationRoute
    @Serializable data object SettingsScreen: NavigationRoute
    @Serializable data class NoteDetailsScreen(val id: Int): NavigationRoute

    @Serializable data object ProfileScreen: NavigationRoute

}