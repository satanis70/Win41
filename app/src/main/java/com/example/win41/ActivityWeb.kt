package com.example.win41

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel


class ActivityWeb : AppCompatActivity() {
    var viewW: WebView? = null
    val ONESIGNAL_APP_ID = "714b9f14-381d-4fc4-a93c-28d480557381"
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    var url: String? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        viewW = findViewById(R.id.viewW)
        url = intent.getStringExtra("url")
        viewW?.settings?.javaScriptEnabled = true
        viewW?.settings?.domStorageEnabled = true
        viewW?.settings?.useWideViewPort = true
        viewW?.settings?.databaseEnabled = true
        viewW?.settings?.builtInZoomControls = true
        viewW?.settings?.cacheMode = WebSettings.LOAD_NO_CACHE
        viewW?.settings?.setSupportZoom(true)
        viewW?.settings?.allowFileAccess = true
        viewW?.settings?.allowContentAccess = true
        viewW?.settings?.loadWithOverviewMode = true
        viewW?.isClickable = true
        viewW?.webViewClient = MyWebViewClient(viewW!!)
        viewW?.webChromeClient = WebChromeCustomClient(
            this,
            findViewById(R.id.frame_layout),
            fileUploadCallback
        )
        viewW?.settings?.supportMultipleWindows()
        viewW?.settings?.allowContentAccess = true
        viewW?.settings?.setNeedInitialFocus(true)
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (viewW!!.canGoBack()) {
            viewW?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        val prefs: SharedPreferences = this.applicationContext
            .getSharedPreferences(this.packageName, MODE_PRIVATE)
        val edit = prefs.edit()
        edit.apply {
            putString("lastUrl", viewW?.url)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewW != null) {
            val prefs: SharedPreferences = this.applicationContext
                .getSharedPreferences(this.packageName, MODE_PRIVATE)
            val s = prefs.getString("lastUrl", "")
            if (s != "") {
                if (s != null) {
                    viewW?.loadUrl(s)
                }
            } else {
                url?.let { viewW?.loadUrl(it) }
            }
        }
    }
}