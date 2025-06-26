package com.omnivoiceai.neuromirror.ui.components.layout.fab.actionsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.omnivoiceai.neuromirror.R

@Composable
fun HomeFabAction(){
    Icon(Icons.Filled.Settings, stringResource(R.string.shopping_cart_button_description))
}