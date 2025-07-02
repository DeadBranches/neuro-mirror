package com.omnivoiceai.neuromirror.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toFormattedLines(): Pair<String, String> {
    val dayFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()) // Tue, Jul 02
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())     // 14:33
    return dayFormat.format(this) to timeFormat.format(this)
}