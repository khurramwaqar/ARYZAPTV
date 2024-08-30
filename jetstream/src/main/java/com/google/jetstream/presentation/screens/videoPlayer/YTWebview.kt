package com.google.jetstream.presentation.screens.videoPlayer

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubeWebViewScreen(videoId: String) {
    Text(text = videoId+"dasdsa", style = TextStyle(Color.Yellow))
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false

            // Create the YouTube iframe URL
            val youTubeUrl = "https://cdn.aryzap.com/api/v3/yt.php?id=s$videoId&dasd=dasdas"

            // Load the URL in the WebView
            loadUrl(youTubeUrl)
        }
    }, modifier = Modifier.fillMaxSize())
}