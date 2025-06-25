package com.omnivoiceai.neuromirror.ui.screens.splash.components

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun GoToHomeButton(navController: NavHostController) {

    fun onClick() {
        navController.navigate(NavigationRoute.HomeScreen)
    };

    Button(
        modifier = Modifier.requiredSize(150.dp, 50.dp),
        onClick = ::onClick
    ){
        Text("Go to homepage")
    }
}