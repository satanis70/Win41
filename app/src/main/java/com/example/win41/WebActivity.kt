package com.example.win41

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class WebActivity : ComponentActivity() {
    var webView: WebView? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val url = "https://www.youtube.com"
            AndroidView(
                factory = { context ->
                    val view = LayoutInflater.from(context).inflate(R.layout.web_layout, null, true)
                    val frameLayout = view.findViewById<FrameLayout>(R.id.frame_layout)
                    webView = view.findViewById(R.id.viewW)
                    webView?.settings?.javaScriptEnabled = true
                    webView?.webChromeClient = WebChromeCustomClient(this@WebActivity, frameLayout)
                    webView?.webViewClient = MyWebViewClient(webView!!)
                    webView?.settings?.loadWithOverviewMode = true
                    webView?.settings?.domStorageEnabled = true
                    webView?.settings?.useWideViewPort = true
                    webView?.settings?.allowContentAccess = true
                    webView?.settings?.supportMultipleWindows()
                    view
                },
                update = {
                    webView?.loadUrl(url)
                }
            )
            if (savedInstanceState != null) {
                webView?.restoreState(savedInstanceState)
            } else {
                webView?.loadUrl(url)
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        webView?.saveState(outState)
        super.onSaveInstanceState(outState)
    }

}

