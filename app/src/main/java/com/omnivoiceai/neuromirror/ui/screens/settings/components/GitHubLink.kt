package com.omnivoiceai.neuromirror.ui.screens.settings.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import com.omnivoiceai.neuromirror.R

@Composable
fun GitHubLink() {
    val context = LocalContext.current
    val url = "https://github.com/OmniVoice-AI/NeuroMirror"

    Text(
        text = stringResource(R.string.github_link),
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            }
    )
}
