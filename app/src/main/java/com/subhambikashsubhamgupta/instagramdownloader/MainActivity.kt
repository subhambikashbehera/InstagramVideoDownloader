git package com.subhambikashsubhamgupta.instagramdownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var requestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        getDownloadableUrl("https://www.instagram.com/reel/CQY8MDfKyw-/")


    }
    fun getDownloadableUrl(url: String?){
        requestQueue = Volley.newRequestQueue(applicationContext)
        val req = JsonObjectRequest(
            Request.Method.GET, "$url?__a=1", null,
            {
                    response ->
                Log.e( "Response",response.toString())

            }, {

            })
        requestQueue?.add(req)
    }
}