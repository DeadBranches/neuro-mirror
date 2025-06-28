package com.omnivoiceai.neuromirror.ui.components.layout.fab

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.components.layout.fab.actionsui.HomeFabAction
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.navigation.hasRoute


@Composable
fun FabActionsForRoute(backStackEntry: NavBackStackEntry?, navController: NavHostController){
    when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> HomeFabAction()
        else -> {}
    }
}