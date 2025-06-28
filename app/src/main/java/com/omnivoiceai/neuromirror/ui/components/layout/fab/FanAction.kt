package com.omnivoiceai.neuromirror.ui.components.layout.fab

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

class FabAction(val backStackEntry: NavBackStackEntry?, val navController: NavHostController){
    fun onClick() {
        return fabActionsForRouteOnClick(backStackEntry = backStackEntry, navController=navController);
    }
    @Composable
    fun Content(){
        return FabActionsForRoute(backStackEntry = backStackEntry, navController=navController);
    }
}