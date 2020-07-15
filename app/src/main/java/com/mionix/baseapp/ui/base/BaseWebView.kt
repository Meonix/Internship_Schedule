package com.mionix.baseapp.ui.base

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.*
import android.webkit.WebView
import com.mionix.baseapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class BaseWebView(){
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    fun setupWebView(url: String,webView:WebView) {
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.apply {
                }
                return true
            }

        }
        webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return try {
                    val httpClient = OkHttpClient()
                    val request: Request = Request.Builder()
                        .url(request.url.toString().trim())
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"
                        )
                        .get()
                        .build()

                    val response: Response = httpClient.newCall(request).execute()
                    WebResourceResponse(
                        getMimeType(request.url.toString()),  // set content-type
                        response.header("Content-Type", "application/json; charset=utf-8"),
                        response.body?.byteStream()
                    )
                } catch (e: Exception) {
                    return super.shouldInterceptRequest(view, request)
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        if (BuildConfig.DEBUG) WebView.setWebContentsDebuggingEnabled(true)

        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true

        webView.addJavascriptInterface(this, "Android")
        val mineType = "text/html"
        val encoding = "UTF-8"
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
        webView.settings.javaScriptEnabled = true
        webView.settings.loadsImagesAutomatically =true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.builtInZoomControls = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadData(url,mineType,encoding)

    }
    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            if (extension == "js") {
                return "text/javascript"
            } else if (extension == "woff") {
                return "application/font-woff"
            } else if (extension == "woff2") {
                return "application/font-woff2"
            } else if (extension == "ttf") {
                return "application/x-font-ttf"
            } else if (extension == "eot") {
                return "application/vnd.ms-fontobject"
            } else if (extension == "svg") {
                return "image/svg+xml"
            } else if (extension == "css") {
                return "text/css"
            } else {
                return "text/html"
            }

            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}