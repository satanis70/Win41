package com.example.win41

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.webkit.WebView

class ActivityWeb : AppCompatActivity() {

    var viewW: WebView? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        viewW = this.findViewById(R.id.viewW)
        val url = "https://www.youtube.com"
        viewW?.settings?.javaScriptEnabled = true
        viewW?.settings?.domStorageEnabled = true
        viewW?.settings?.useWideViewPort = true
        viewW?.settings?.databaseEnabled = true
        viewW?.settings?.builtInZoomControls = true
        viewW?.settings?.cacheMode = WebSettings.LOAD_NO_CACHE
        viewW?.settings?.setSupportZoom(true)
        viewW?.settings?.allowFileAccess = true
        viewW?.settings?.loadWithOverviewMode = true
        viewW?.isClickable = true
        viewW?.webChromeClient = WebChromeCustomClient(this, findViewById(R.id.frame_layout))
        viewW?.webViewClient = MyWebViewClient(viewW!!)
        viewW?.settings?.supportMultipleWindows()
        viewW?.settings?.allowContentAccess = true
        viewW?.settings?.setNeedInitialFocus(true)
        checkInstance(viewW!!, savedInstanceState, url)
    }

    private fun checkInstance(viewW: WebView, savedInstanceState: Bundle?, url: String){
        if (savedInstanceState != null){
            viewW.restoreState(savedInstanceState)
        } else {
            viewW.loadUrl(url)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewW?.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (viewW!!.canGoBack()) {
            viewW?.goBack()
        } else {
            super.onBackPressed()
        }
    }
}