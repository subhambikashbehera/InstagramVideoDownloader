package com.subhambikashsubhamgupta.instagramdownloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class DownloadFragment : Fragment() {


    private var param1: String? = null
    private var param2: String? = null
    private val mParam1: String? = null
    private val mParam2: String? = null
    private var download: Button? = null
    private var eturl: EditText? = null
    private var webView: WebView? = null
    private var requestQueue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_download, container, false)
        eturl = view.findViewById(R.id.eturl)
        download = view.findViewById(R.id.download)
        webView = view.findViewById(R.id.webview)
        //call getDownloadableUrl(eturl.getText)
        download?.setOnClickListener { getDownloadableUrl(eturl?.text.toString()) }
        return view
    }


    fun getDownloadableUrl(url: String?){
        requestQueue = Volley.newRequestQueue(context)
        val req = JsonObjectRequest(Request.Method.GET, url, null,
            {
            response ->
                Log.e( "Response",response["success"].toString())
        }, {

            })
        requestQueue?.add(req)
    }

    fun downloadFile(context: Context, fileName: String?, url: String?) {
        val downloadmanager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadmanager.enqueue(request)
    }
}