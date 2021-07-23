package com.subhambikashsubhamgupta.instagramdownloader

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Privacypolicies : AppCompatActivity() {


    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacypolicies)

        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        webView=findViewById(R.id.webview)

        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl("file:///android_asset/Privacy Policy.html")


    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}