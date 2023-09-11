package com.example.win41

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher

class WebChromeCustomClient(
    private val activity: Activity,
    private val fullscreenContainer: FrameLayout,
    private val fileUploadActivityResultLauncher: ActivityResultLauncher<Intent>,
    private var fileUploadCallback: ValueCallback<Array<Uri>>?
):WebChromeClient() {

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalOrientation = 0

    override fun onHideCustomView() {
        if (customView == null) {
            return
        }
        fullscreenContainer.removeView(customView)
        customView = null
        customViewCallback!!.onCustomViewHidden()
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            onHideCustomView()
            return
        }
        customView = view
        originalOrientation = activity.requestedOrientation
        customViewCallback = callback
        fullscreenContainer.addView(
            customView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        fullscreenContainer.visibility = View.VISIBLE
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

   /* override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        fileUploadCallback = filePathCallback ?: return false
        val intent = fileChooserParams?.createIntent()
        fileUploadActivityResultLauncher.launch(intent)
        return true
    }*/


}