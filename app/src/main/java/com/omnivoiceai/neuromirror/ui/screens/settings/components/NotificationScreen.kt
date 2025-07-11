package com.omnivoiceai.neuromirror.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable

@Composable
fun NotificationScreen() {
    Column(){
        NotificationPreferences()
        HorizontalDivider()
        NotificationTest()
    }
}