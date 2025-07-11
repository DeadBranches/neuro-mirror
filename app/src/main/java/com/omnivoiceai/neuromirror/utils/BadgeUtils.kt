package com.omnivoiceai.neuromirror.utils

import androidx.compose.ui.graphics.Color
import com.omnivoiceai.neuromirror.ui.theme.BasicBadge
import com.omnivoiceai.neuromirror.ui.theme.BronzeBadge
import com.omnivoiceai.neuromirror.ui.theme.GenericBadge
import com.omnivoiceai.neuromirror.ui.theme.GoldBadge
import com.omnivoiceai.neuromirror.ui.theme.SilverBadge

fun getBadgeColorForLevel(level: Int): Color {
    return when (level) {
        10 -> GoldBadge
        5 -> SilverBadge
        3 -> BronzeBadge
        1 -> BasicBadge
        else -> GenericBadge
    }
}
