package com.omnivoiceai.neuromirror.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    Column(){
        NotificationPreferences()
        HorizontalDivider()
        NotificationTest()
    }
}