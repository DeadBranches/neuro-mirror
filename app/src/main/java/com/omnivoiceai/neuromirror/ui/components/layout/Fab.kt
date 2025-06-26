package com.omnivoiceai.neuromirror.ui.components.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.navigation.hasRoute
import com.omnivoiceai.neuromirror.utils.Logger
import androidx.compose.ui.res.stringResource
import com.omnivoiceai.neuromirror.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Fab(navController: NavHostController, modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Logger.d("🧭 Current destination route: ${backStackEntry?.destination?.route}")

    return FloatingActionButton(
        onClick = { fabActionsForRouteOnClick(backStackEntry = backStackEntry, navController=navController) },
        content = { FabActionsForRoute(backStackEntry = backStackEntry, navController=navController) },
    )
}

@Composable
fun FabActionsForRoute(backStackEntry: NavBackStackEntry?, navController: NavHostController){
    when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> HomeFabAction()
    }
}

@Composable
fun HomeFabAction(){
    Icon(Icons.Filled.Settings, stringResource(R.string.shopping_cart_button_description))
}

fun fabActionsForRouteOnClick(backStackEntry: NavBackStackEntry?, navController: NavHostController){
    when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> homeFabActionClick(navController)
    }
}

fun homeFabActionClick(navController: NavHostController){
    navController.navigate(NavigationRoute.SettingsScreen)
}