package com.omnivoiceai.neuromirror.domain.model

import androidx.annotation.StringRes
import com.omnivoiceai.neuromirror.R

enum class LoadingMessages(@StringRes val labelResId: Int) {
    GeneratingQuestion(R.string.generation_questions),
}
