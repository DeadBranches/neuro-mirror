package com.omnivoiceai.neuromirror.ui.screens.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.screens.splash.components.GoToHomeButton


@Composable
fun SplashScreen(navController: NavHostController, modifier: Modifier = Modifier ){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to NeuroMirror", style = MaterialTheme.typography.headlineLarge)
        Text("A cognitive-emotional diary that reflects who you are", textAlign = TextAlign.Center, fontStyle = FontStyle.Italic)
        Button(onClick = { navController.navigate(NavigationRoute.LoginScreen) }, modifier = Modifier.fillMaxWidth()) {Text("Login") }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {Text("Register") }
        GoToHomeButton(navController = navController)
    }
}
