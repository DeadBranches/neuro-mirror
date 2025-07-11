package com.omnivoiceai.neuromirror.domain.model

import com.omnivoiceai.neuromirror.BuildConfig

object Config {
    val BASE_URL = if(BuildConfig.DEBUG) "http://192.168.1.226:8000/" else "https://api.ai.digitalnext.business/"
    const val DATABASE_NAME = "note-list"
    const val PREFERENCE_DATA_STORE = "note-list"
}