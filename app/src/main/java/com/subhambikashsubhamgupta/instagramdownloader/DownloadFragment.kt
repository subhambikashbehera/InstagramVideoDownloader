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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject


class DownloadFragment : Fragment() {

    private var download: ImageButton? = null
    private var eturl: EditText? = null
    lateinit var imageview:ImageView
    lateinit var mediaController: MediaController
    private lateinit var videoView: VideoView
    lateinit var generate:Button
    var downloadableurl=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_download, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eturl = view.findViewById(R.id.eturl)
        download = view.findViewById(R.id.download)
        videoView = view.findViewById(R.id.videoView2)
        imageview=view.findViewById(R.id.imageView)
        generate=view.findViewById(R.id.generate)


        generate.setOnClickListener {
            hidekeyboard()
            getDownloadableUrl(eturl?.text.toString()) }

        download?.setOnClickListener {
            videoView.start()
            activity?.applicationContext?.let { downloadFile(it,"instasaver.mp4",downloadableurl) }
        }


    }

    fun getDownloadableUrl(url: String?){
        val uri: Uri
        uri = Uri.parse(url)
        val path = uri.pathSegments
        println("path"+path)
        val url_final = "https://www.instagram.com/"+path[0]+"/"+path[1]+"/?__a=1"
        println(url_final)
       val requestQueue = Volley.newRequestQueue(context)

        val stringRequest=object :StringRequest(Request.Method.GET,url_final,Response.Listener<String>
        {response ->

                var video_url=""
                var pic_url=""

                try {
                    val json = JSONObject(response)
                    val ur0 = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                        .getJSONArray("display_resources")
                    val jsonobject2 = ur0.getJSONObject(2)
                    pic_url = jsonobject2.getString("src")
                    Log.d("pic", pic_url);
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }

            try {
                val json = JSONObject(response.toString())
                video_url = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                    .getString("video_url")
                downloadableurl=video_url
                Log.e("vid_url", video_url);
            }catch (e:Exception)
            {
                e.printStackTrace()
            }


            if (video_url!=""){
                    videoView?.visibility=View.VISIBLE
                    imageview.visibility=View.GONE

                videoView.setVideoURI(Uri.parse(video_url))
                mediaController= MediaController(context)
                mediaController.setAnchorView(videoView)
                videoView.setMediaController(mediaController)

                videoView.setOnPreparedListener {
                   it.isLooping=true
                    it.start()
                }
                }else
            {
                videoView.visibility =View.GONE
                imageview.visibility=View.VISIBLE


                Glide
                    .with(this)
                    .load(pic_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageview);
            }



        },
            Response.ErrorListener { error ->
                Log.e("Error", error.message.toString())


            })
        {

        }

        requestQueue.add(stringRequest)

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


    fun hidekeyboard() {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken,
            InputMethodManager.SHOW_FORCED)
    }


}