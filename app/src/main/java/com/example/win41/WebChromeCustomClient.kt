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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class WebChromeCustomClient(
    activity: Activity,
    private val fullscreenContainer: FrameLayout,
    private var fileChooserValueCallback: ValueCallback<Array<Uri>>? = null
) : WebChromeClient() {

    private val _activity = activity as AppCompatActivity
    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var originalOrientation = 0
    private var fileChooserResultLauncher = createFileChooserResultLauncher()

    override fun onHideCustomView() {
        if (customView == null) {
            return
        }
        fullscreenContainer.removeView(customView)
        customView = null
        customViewCallback!!.onCustomViewHidden()
        _activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        _activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (customView != null) {
            onHideCustomView()
            return
        }
        customView = view
        originalOrientation = _activity.requestedOrientation
        customViewCallback = callback
        fullscreenContainer.addView(
            customView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        fullscreenContainer.visibility = View.VISIBLE
        _activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        _activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        fileChooserValueCallback = filePathCallback;
        fileChooserResultLauncher.launch(fileChooserParams?.createIntent())
        return true
    }

    private fun createFileChooserResultLauncher(): ActivityResultLauncher<Intent> {
        return _activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fileChooserValueCallback?.onReceiveValue(arrayOf(Uri.parse(it?.data?.dataString)));
            } else {
                fileChooserValueCallback?.onReceiveValue(null)
            }
        }
    }



}