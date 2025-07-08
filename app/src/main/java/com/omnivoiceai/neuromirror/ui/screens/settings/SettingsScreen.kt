package com.omnivoiceai.neuromirror.ui.screens.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.notifications.NotificationScheduler
import com.omnivoiceai.neuromirror.ui.screens.auth.login.LoginViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.components.NotificationTestSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeSettings
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel

@Composable
fun SettingsScreen(loginViewModel: LoginViewModel, navController: NavController, themeViewModel: ThemeViewModel){
    val themeState by themeViewModel.state.collectAsStateWithLifecycle()
    val currentUser by loginViewModel.currentUser.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, 8.dp)
    ){
        Text(
            stringResource(R.string.settings_page_theme_title),
            style = MaterialTheme.typography.titleLarge
        )
        ThemeSettings(themeState, themeViewModel::changeTheme)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            stringResource(R.string.settings_notifications),
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        NotificationTestSettings(
            onTestNotification = {
                // Check permission explicitly before scheduling
                val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    true // Permission not required for API < 33
                }
                
                if (hasPermission) {
                    try {
                        val scheduler = NotificationScheduler(context)
                        scheduler.scheduleTestNotification()
                        Toast.makeText(
                            context,
                            context.getString(R.string.test_notification_scheduled),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: SecurityException) {
                        Toast.makeText(
                            context,
                            "Notification permission required",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please grant notification permission in app settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if(currentUser != null) {
            Button(onClick = { loginViewModel.signOut() }) {
                Text("Logout")
            }
        }
    }
}