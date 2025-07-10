package com.omnivoiceai.neuromirror.ui.components.notifications

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omnivoiceai.neuromirror.utils.UiEventBus
import com.omnivoiceai.neuromirror.utils.toUiNotification

@Composable
fun HandleUiEvents(snackbarHostState: SnackbarHostState) {
    LaunchedEffect(Unit) {
        UiEventBus.events.collect { event ->
            val data = event.toUiNotification() // ✅ Ora è una normale funzione

            // Facoltativo: se vuoi tradurre `badge.badgeKey`, fallo altrove (es. nel rendering)
            val prefix = data?.emoji?.let { "$it " } ?: ""
            snackbarHostState.showSnackbar(prefix + data?.message.orEmpty())
        }
    }
}
