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
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class DownloadFragment : Fragment() {


    private var param1: String? = null
    private var param2: String? = null
    private val mParam1: String? = null
    private val mParam2: String? = null
    private var download: Button? = null
    private var eturl: EditText? = null
    private var videoView: WebView? = null
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
        videoView = view.findViewById(R.id.webview)

        //call getDownloadableUrl(eturl.getText)
        download?.setOnClickListener { getDownloadableUrl(eturl?.text.toString()) }
        return view
    }


    fun getDownloadableUrl(url: String?){
        var uri: Uri
        uri = Uri.parse(url)
        var path = uri.pathSegments
        println("path"+path)
        var url_final = "https://www.instagram.com/"+path[0]+"/"+path[1]+"/?__a=1/"
        println(url_final)
        requestQueue = Volley.newRequestQueue(context)
        val req = JsonObjectRequest(Request.Method.GET, url_final, null,
            {
                    response ->
                var video_url=""
                var pic_url=""
                try {
                    Log.e( "Response",response.toString())
                    var json: JSONObject = JSONObject(response.toString())
                    video_url = json.getJSONObject("graphql").getJSONObject("shortcode_media").getString("video_url")
                    Log.e("vid_url", video_url);
                    var ur0 = json.getJSONObject("graphql").getJSONObject("shortcode_media").getString("display_resources")
                    var ur1 = ur0[2]
                    var pu :JSONObject = JSONObject(ur1.toString())
                    pic_url = pu.getString("src")
                    Log.e("pic",pic_url);

                }catch (e:Exception){
                    e.message?.let { Log.e("Error", it) }
                }
                if (video_url.isNotEmpty()){
                    videoView?.loadUrl(video_url)
                    activity?.applicationContext?.let { downloadFile(it,"instasaver.mp4",video_url) }

                }

                else if (pic_url.isNotEmpty()){

                }
                else
                    Toast.makeText(context,"No Video or Pic Available",Toast.LENGTH_LONG).show()


            }, {
                    VolleyError->
                Log.e("Error", VolleyError.message.toString())
                Log.e("Error", VolleyError.localizedMessage)
                videoView?.loadUrl(url_final)

            })

        requestQueue?.add(req)
    }



    fun downloadFile(context: Context, fileName: String?, url: String?) {
        if (url != null) {
            Log.e("url",url)
        }
        val downloadmanager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadmanager.enqueue(request)
    }

}