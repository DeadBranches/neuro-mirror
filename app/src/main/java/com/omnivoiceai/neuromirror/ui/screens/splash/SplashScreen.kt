package com.omnivoiceai.neuromirror.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.components.layout.EmptySpacer
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute
import com.omnivoiceai.neuromirror.ui.screens.splash.components.GoToHomeButton


@Composable
fun SplashScreen(navController: NavHostController, modifier: Modifier = Modifier ){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to NeuroMirror", style = MaterialTheme.typography.headlineLarge)
        Text(
            stringResource(R.string.app_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.bodyLarge
        )
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "description",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .fillMaxWidth(0.7f)
                .padding(vertical = 32.dp).clip(RoundedCornerShape(10))
        )
        Button(
            onClick = { navController.navigate(NavigationRoute.LoginScreen) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.login))
        }
        Button(
            onClick = { navController.navigate(NavigationRoute.RegisterScreen) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.register))
        }
        EmptySpacer()
        GoToHomeButton(navController = navController)
    }
}
