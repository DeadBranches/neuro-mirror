package com.omnivoiceai.neuromirror.ui.components.forms

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkdownText(
    text: String,
    isUser: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        var currentIndex = 0

        val regex = Regex("""(\*\*.*?\*\*|\*.*?\*|\[.*?\]\(.*?\))""")
        val matches = regex.findAll(text)

        for (match in matches) {
            val matchStart = match.range.first
            val matchEnd = match.range.last + 1

            if (currentIndex < matchStart) {
                append(text.substring(currentIndex, matchStart))
            }

            val matchText = match.value

            when {
                matchText.startsWith("**") -> {
                    val content = matchText.removeSurrounding("**")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(content)
                    }
                }

                matchText.startsWith("*") -> {
                    val content = matchText.removeSurrounding("*")
                    withStyle(SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                        append(content)
                    }
                }

                matchText.startsWith("[") && matchText.contains("](") -> {
                    val label = matchText.substringAfter("[").substringBefore("]")
                    val url = matchText.substringAfter("(").substringBefore(")")

                    val start = length
                    append(label)
                    addStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        start,
                        start + label.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = url,
                        start = start,
                        end = start + label.length
                    )
                }
            }

            currentIndex = matchEnd
        }

        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }

    Text(
        text = annotatedText,
        modifier =  Modifier.padding(12.dp),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            color = if (isUser)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        ),
    )
}
