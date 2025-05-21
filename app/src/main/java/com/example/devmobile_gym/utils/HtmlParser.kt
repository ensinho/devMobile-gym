package com.example.devmobile_gym.utils

import android.text.Html
import androidx.compose.ui.text.AnnotatedString

fun parseHtmlToAnnotated(html: String): AnnotatedString {
    val spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    return AnnotatedString(spanned.toString()) // Simples. Pode ser melhorado com span custom
}
