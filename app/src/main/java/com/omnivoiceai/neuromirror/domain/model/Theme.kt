package com.omnivoiceai.neuromirror.domain.model

import androidx.annotation.StringRes
import com.omnivoiceai.neuromirror.R

enum class Theme (@StringRes val labelResId: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    System(R.string.theme_system)
}
