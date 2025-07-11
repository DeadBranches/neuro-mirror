package com.omnivoiceai.neuromirror.ui.screens.splash.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun GoToHomeButton(navController: NavHostController) {

    fun onClick() {
        navController.navigate(NavigationRoute.HomeScreen, {
            popUpTo(0) { inclusive = true }
        })
    };

    Text(
        stringResource(R.string.not_now),
        style = TextStyle(textDecoration = TextDecoration.Underline),
        modifier = Modifier.clickable { ::onClick.invoke() }
    )
}