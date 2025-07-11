package com.omnivoiceai.neuromirror.domain.model

import com.omnivoiceai.neuromirror.BuildConfig

object Config {

    const val FORCE_REMOTE_URL = true

    private const val LOCAL_URL = "http://192.168.1.226:8000/"
    private const val REMOTE_URL = "https://api.ai.digitalnext.business/"
    val BASE_URL = when {
        FORCE_REMOTE_URL -> REMOTE_URL
        BuildConfig.DEBUG -> LOCAL_URL
        else -> REMOTE_URL
    }
    const val DATABASE_NAME = "note-list"
    const val PREFERENCE_DATA_STORE = "note-list"
}