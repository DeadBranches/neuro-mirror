package com.omnivoiceai.neuromirror.utils
import com.omnivoiceai.neuromirror.BuildConfig
import android.util.Log
object Logger {

    private fun caller(): String {
        return Throwable().stackTrace
            .firstOrNull { it.className != Logger::class.java.name }
            ?.let { "${it.fileName}:${it.lineNumber}" }
            ?: "Unknown"
    }

    private inline fun ifDebug(block: () -> Unit){
        if(BuildConfig.DEBUG){
            block();
        }
    }

    fun d(message: String) =  ifDebug(){ Log.d(caller(), message) }
    fun info(message: String) = ifDebug(){ Log.i(caller(), message) }

    fun error(message: String, throwable: Throwable? = null) = ifDebug(){ Log.e(caller(), message, throwable) }
}