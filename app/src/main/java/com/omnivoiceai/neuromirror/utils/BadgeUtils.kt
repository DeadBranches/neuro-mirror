package com.omnivoiceai.neuromirror.utils

import androidx.compose.ui.graphics.Color

fun getBadgeColorForLevel(level: Int): Color {
    return when (level) {
        10 -> Color(0xFFFFC107)
        5 -> Color(0xFFB0BEC5)
        3 -> Color(0xFFB87333)
        1 -> Color(0xFF42A5F5)
        else -> Color(0xFFBDBDBD)
    }
}
