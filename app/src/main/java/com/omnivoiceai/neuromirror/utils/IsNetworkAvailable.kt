package com.omnivoiceai.neuromirror.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.omnivoiceai.neuromirror.R

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities?.run {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false
}


inline fun guardOnlineOrShowError(context: Context, onAvailable: () -> Unit) {
    if (!isNetworkAvailable(context)) {
        UiEventBus.showError(context.getString(R.string.error_network))
    } else {
        onAvailable()
    }
}