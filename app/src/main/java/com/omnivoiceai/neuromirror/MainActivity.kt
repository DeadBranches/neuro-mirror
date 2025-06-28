package com.omnivoiceai.neuromirror

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omnivoiceai.neuromirror.domain.model.Theme
import com.omnivoiceai.neuromirror.ui.components.layout.AppBar
import com.omnivoiceai.neuromirror.ui.components.layout.fab.Fab
import com.omnivoiceai.neuromirror.ui.navigation.NavGraph
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.navigation.hasRoute
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import com.omnivoiceai.neuromirror.ui.theme.NeuroMirrorTheme
import com.omnivoiceai.neuromirror.utils.Logger
import org.koin.androidx.compose.koinViewModel

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("On Create called")
        Toast.makeText(this, "$TAG onCreate", Toast.LENGTH_LONG).show()

        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            NeuroMirrorTheme(
                darkTheme = themeState.isDarkTheme
            ) {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()

                val appBarVisible = remember(backStackEntry) {
                    when {
                        backStackEntry?.destination?.hasRoute<NavigationRoute.SplashScreen>() == true -> false
                        else -> true
                    }
                }

                val fabVisible = remember(backStackEntry) {
                    when {
                        backStackEntry?.destination?.hasRoute<NavigationRoute.SplashScreen>() == true -> false
                        backStackEntry?.destination?.hasRoute<NavigationRoute.SettingsScreen>() == true -> false
                        else -> true
                    }
                }

                Scaffold(
                    topBar = { if(appBarVisible) AppBar(navController) },
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = { if(fabVisible) Fab(navController = navController) }
                ) { innerPadding ->
                    NavGraph(navController, theme=themeViewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Logger.d("On start called")
        Toast.makeText(this, "$TAG onStart", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        Logger.d("On resume called")
        Toast.makeText(this, "$TAG onResume", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Logger.d("On pause called")
        Toast.makeText(this, "$TAG onPause", Toast.LENGTH_LONG).show()
    }
}

