package com.omnivoiceai.neuromirror.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

fun Context.updateLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration(resources.configuration)
    config.setLocale(locale)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        createConfigurationContext(config)
    } else {
        resources.updateConfiguration(config, resources.displayMetrics)
        this
    }
}
