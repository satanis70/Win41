package com.example.win41

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

class MyWebViewClient(val mWebView: WebView): WebViewClient() {
    private var mHasToRestoreState = false
    private val mProgressToRestore = 0f
    override fun onPageFinished(view: WebView, url: String?) {
        if (mHasToRestoreState) {
            mHasToRestoreState = false
            view.postDelayed(
                {
                    val webviewsize: Int = mWebView.contentHeight - mWebView.top
                    val positionInWV: Float = webviewsize * mProgressToRestore
                    val positionY = (mWebView.top + positionInWV).roundToInt()
                    mWebView.scrollTo(0, positionY)
                },
                300
            )
        }
        super.onPageFinished(view, url)
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url!!)
        Log.i("ERRORWEBVIEW", url.toString())
        return true
    }
}