package com.omnivoiceai.neuromirror.ui.screens.splash.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun GoToHomeButton(navController: NavHostController) {

    fun onClick() {
        navController.navigate(NavigationRoute.HomeScreen, {
            popUpTo(0) { inclusive = true }
        })
    };

    Text("Not Now!",
        style = TextStyle(textDecoration = TextDecoration.Underline),
        modifier = Modifier.clickable { ::onClick.invoke() }
    )
}