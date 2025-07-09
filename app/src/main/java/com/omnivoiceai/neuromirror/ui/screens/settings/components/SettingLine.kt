package com.omnivoiceai.neuromirror.ui.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun SettingLineItem(name: String, destination: NavigationRoute.LoginScreen, navController: NavHostController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().clickable(){navController.navigate(destination)}.padding(vertical = 16.dp)
    ){
        Text(name, modifier = Modifier)
        Icon(
            Icons.Default.ArrowForwardIos,
            contentDescription = "",
            modifier = Modifier.size(16.dp),
        )
    }
}