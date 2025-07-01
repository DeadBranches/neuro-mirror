package com.omnivoiceai.neuromirror.ui.components.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun EmptySpacer(modifier: Modifier = Modifier, width: Dp = LocalConfiguration.current.screenWidthDp.dp, height: Dp = 16.dp){
    Spacer(modifier = Modifier.width(width).height(height))
}