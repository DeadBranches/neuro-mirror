package com.omnivoiceai.neuromirror.ui.components.layout.fab

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.components.layout.fab.actions.homeFabActionClick
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.navigation.hasRoute

fun fabActionsForRouteOnClick(backStackEntry: NavBackStackEntry?, navController: NavHostController){
    when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> homeFabActionClick(navController)
        else -> {}
    }
}

