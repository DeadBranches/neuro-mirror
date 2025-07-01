package com.omnivoiceai.neuromirror.ui.components.layout.fab

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omnivoiceai.neuromirror.utils.Logger


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Fab(navController: NavHostController, modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Logger.d("🧭 Current destination route: ${backStackEntry?.destination?.route}")

    val fabAction = FabAction(backStackEntry=backStackEntry, navController=navController);

    return FloatingActionButton(
        onClick = { fabAction.onClick() },
        content = {fabAction.Content() },
    )
}

