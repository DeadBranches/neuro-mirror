package com.omnivoiceai.neuromirror

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omnivoiceai.neuromirror.ui.components.layout.AppBar
import com.omnivoiceai.neuromirror.ui.components.layout.fab.Fab
import com.omnivoiceai.neuromirror.ui.components.notifications.HandleUiEvents
import com.omnivoiceai.neuromirror.ui.navigation.NavGraph
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.navigation.hasRoute
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import com.omnivoiceai.neuromirror.ui.theme.NeuroMirrorTheme
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.utils.updateLocale
import org.koin.androidx.compose.koinViewModel

private const val TAG = "MainActivity"

val LocalAppContext = staticCompositionLocalOf<Context> {
    error("LocalAppContext not provided")
}

class MainActivity : ComponentActivity() {
    
    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Permission result handled automatically by system
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(
                LocalAppContext provides this
            ) {
                val themeViewModel = koinViewModel<ThemeViewModel>()
                val themeState by themeViewModel.state.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarColor = remember { mutableStateOf(Color.Unspecified) }

                HandleUiEvents(snackbarHostState, snackbarColor)


                NeuroMirrorTheme(
                    darkTheme = themeState.isDarkTheme
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()

                    val appBarVisible = remember(backStackEntry) {
                        when {
                            backStackEntry?.destination?.hasRoute<NavigationRoute.SplashScreen>() == true -> false
                            backStackEntry?.destination?.hasRoute<NavigationRoute.LoginScreen>() == true -> false
                            backStackEntry?.destination?.hasRoute<NavigationRoute.RegisterScreen>() == true -> false
                            else -> true
                        }
                    }

                    val fabVisible = remember(backStackEntry) {
                        Logger.info(backStackEntry?.destination?.hasRoute<NavigationRoute.NoteDetailsScreen>().toString())
                        when {
                            backStackEntry?.destination?.hasRoute<NavigationRoute.HomeScreen>() == true -> true
                            else -> false
                        }
                    }

                    Scaffold(
                        topBar = { if(appBarVisible) AppBar(navController) },
                        modifier = Modifier.fillMaxSize(),
                        floatingActionButton = { if(fabVisible) Fab(navController = navController) },
                        snackbarHost = {
                            SnackbarHost(snackbarHostState) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = snackbarColor.value,
                                    contentColor = Color.White
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavGraph(
                            navController,
                            theme=themeViewModel,
                            modifier = Modifier.padding(innerPadding).imePadding()
                        )
                    }
                }
            }
        }
    }


    override fun attachBaseContext(newBase: Context) {
        val language = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getString("language", "en") ?: "en"
        val updatedContext = newBase.updateLocale(language)
        super.attachBaseContext(updatedContext)
    }

}