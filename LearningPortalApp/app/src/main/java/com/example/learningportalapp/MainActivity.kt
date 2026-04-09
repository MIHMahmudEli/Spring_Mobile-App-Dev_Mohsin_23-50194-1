package com.example.learningportalapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var addressBar: EditText
    private lateinit var btnGo: Button
    private lateinit var btnBack: Button
    private lateinit var btnForward: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnHome: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var shortcutGoogle: Button
    private lateinit var shortcutYouTube: Button
    private lateinit var shortcutWikipedia: Button
    private lateinit var shortcutKhanAcademy: Button
    private lateinit var shortcutUniversity: Button

    private val defaultUrl = "https://www.aiub.edu"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        webView = findViewById(R.id.web_view)
        addressBar = findViewById(R.id.et_address_bar)
        btnGo = findViewById(R.id.btn_go)
        btnBack = findViewById(R.id.btn_back)
        btnForward = findViewById(R.id.btn_forward)
        btnRefresh = findViewById(R.id.btn_refresh)
        btnHome = findViewById(R.id.btn_home)
        progressBar = findViewById(R.id.progress_bar)

        shortcutGoogle = findViewById(R.id.shortcut_google)
        shortcutYouTube = findViewById(R.id.shortcut_youtube)
        shortcutWikipedia = findViewById(R.id.shortcut_wikipedia)
        shortcutKhanAcademy = findViewById(R.id.shortcut_khan_academy)
        shortcutUniversity = findViewById(R.id.shortcut_university)

        // WebView configuration
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                addressBar.setText(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                // Show offline page when error occurs (e.g., no internet)
                webView.loadUrl("file:///android_asset/offline.html")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }

        // Load default URL
        webView.loadUrl(defaultUrl)

        // Set button click listeners
        btnBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                Toast.makeText(this, "No more history", Toast.LENGTH_SHORT).show()
            }
        }

        btnForward.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }

        btnRefresh.setOnClickListener {
            webView.reload()
        }

        btnHome.setOnClickListener {
            loadUrl(defaultUrl)
        }

        btnGo.setOnClickListener {
            val urlString = addressBar.text.toString()
            loadUrl(urlString)
        }

        // Handle address bar "Go" action on keyboard
        addressBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                loadUrl(addressBar.text.toString())
                true
            } else {
                false
            }
        }

        // Shortcut buttons listeners
        shortcutGoogle.setOnClickListener { loadUrl("https://www.google.com") }
        shortcutYouTube.setOnClickListener { loadUrl("https://www.youtube.com") }
        shortcutWikipedia.setOnClickListener { loadUrl("https://www.wikipedia.org") }
        shortcutKhanAcademy.setOnClickListener { loadUrl("https://www.khanacademy.org") }
        shortcutUniversity.setOnClickListener { loadUrl("https://www.aiub.edu") }

        // Handle system back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun loadUrl(url: String) {
        var formattedUrl = url
        if (formattedUrl.isNotEmpty()) {
            if (!formattedUrl.startsWith("http://") && !formattedUrl.startsWith("https://")) {
                formattedUrl = "https://$formattedUrl"
            }
            webView.loadUrl(formattedUrl)
        }
    }
}