package com.omnivoiceai.neuromirror.domain.model

import androidx.annotation.StringRes
import com.omnivoiceai.neuromirror.R

enum class Language(val code: String, @StringRes val labelResId: Int) {
    English("en", R.string.language_english),
    Italian("it", R.string.language_italian)
}
