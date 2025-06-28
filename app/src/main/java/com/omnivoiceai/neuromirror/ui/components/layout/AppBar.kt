package com.omnivoiceai.neuromirror.ui.components.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun AppBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Logger.d("🧭 Current destination route: ${backStackEntry?.destination?.route}")

    val title = when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.SplashScreen>() == true -> stringResource(
            R.string.splash_screen_name)
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> stringResource(
            R.string.home_screen_name)
        else -> null
    }

    return TopAppBar(
        title = {
            title?.let {
                Text(it)
            }
        },
        navigationIcon = {
            if (
                navController.previousBackStackEntry != null &&
                backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == false
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(
                        R.string.back_button_description))
                }
            }
            if(backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Menu, stringResource(R.string.menu_button_description))
                }
            }
        },
        actions = { AppBarActionsForRoute(backStackEntry) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        )
    )
}

@Composable
fun AppBarActionsForRoute(backStackEntry: NavBackStackEntry?){
    when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> HomeActions()
    }
}

@Composable
fun HomeActions(){
    IconButton(onClick = { /*TODO*/ }) {
        Icon(Icons.Filled.ShoppingCart, stringResource(R.string.shopping_cart_button_description))
    }
    IconButton(onClick = { /*TODO*/ }) {
        Icon(Icons.Filled.Favorite, stringResource(R.string.favorite_button_description))
    }
}