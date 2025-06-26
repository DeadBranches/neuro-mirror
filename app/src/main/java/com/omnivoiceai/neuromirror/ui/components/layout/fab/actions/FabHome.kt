package com.omnivoiceai.neuromirror.ui.components.layout.fab.actions

import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

fun homeFabActionClick(navController: NavHostController){
    navController.navigate(NavigationRoute.SettingsScreen)
}