package com.omnivoiceai.neuromirror.ui.components.layout

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBackIosNew
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.navigation.hasRoute
import com.omnivoiceai.neuromirror.utils.Logger


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Logger.d("🧭 Current destination route: ${backStackEntry?.destination?.route}")
    Logger.d("🧭 Current destination route: ${NavigationRoute.SettingsSubScreen}")
    Logger.d("🧭 Current destination route: ${backStackEntry?.arguments?.getString("title")}")

    val title = when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.SplashScreen>() == true -> stringResource(
            R.string.splash_screen_name)
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> stringResource(
            R.string.home_screen_name)
        backStackEntry?.destination?.hasRoute<NavigationRoute.SettingsScreen>() == true -> stringResource(
            R.string.settings_page_title)
        backStackEntry?.destination?.hasRoute<NavigationRoute.ProfileScreen>() == true -> stringResource(
            R.string.profile_screen_name)
        backStackEntry?.destination?.route?.contains("ChatScreen") == true -> stringResource(
            R.string.chat_screen_name)
        backStackEntry?.arguments?.getString("title") == "Language" -> stringResource(
            R.string.language)
        else -> backStackEntry?.arguments?.getString("title")
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
                    Icon(Icons.Outlined.ArrowBackIosNew, stringResource(R.string.back_button_description), )
                }
            }
        },
        actions = { AppBarActionsForRoute(navController, backStackEntry) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        )
    )
}

@Composable
fun AppBarActionsForRoute(navController: NavHostController, backStackEntry: NavBackStackEntry?){
    when {
        backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> HomeActions(navController)
    }
}

@Composable
fun HomeActions(navController: NavHostController){
    IconButton(onClick = { navController.navigate(NavigationRoute.ProfileScreen) }) {
        Icon(Icons.Filled.AccountCircle, stringResource(R.string.profile_button_description), modifier = Modifier.size(24.dp))
    }
}