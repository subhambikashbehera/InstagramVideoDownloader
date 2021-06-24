package com.subhambikashsubhamgupta.instagramdownloader

import android.app.DownloadManager
import android.app.Service
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject


class DownloadFragment : Fragment() {


    private var download: Button? = null
    private var share: Button? = null
    private var fromclip: Button? = null
    private var progress: ProgressBar? = null
    private var eturl: EditText? = null
    lateinit var imageview:ImageView
    lateinit var mediaController: MediaController
    private lateinit var videoView: VideoView
    lateinit var generate:Button
    var downloadableurl=""
    var id:Long=0
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
        share=view.findViewById(R.id.share)
        progress=view.findViewById(R.id.progress)
        fromclip=view.findViewById(R.id.pastefromclip)
        progress?.isIndeterminate = true


        generate.setOnClickListener {
            hidekeyboard()
           getDownloadableUrl(eturl?.text.toString())
            progress?.visibility = View.VISIBLE
            /*getJsonResponse(eturl?.text.toString()) */}

        download?.setOnClickListener {
            videoView.start()
            activity?.applicationContext?.let { downloadFile(it,"instasaver.mp4",downloadableurl) }
        }
        fromclip?.setOnClickListener {
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            eturl?.setText(clipboard.text as String?)
        }



        val reciver=object : BroadcastReceiver()
        {
            override fun onReceive(context: Context?, intent: Intent?) {
                val getid=intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
                if (getid==id)
                {
                    Toast.makeText(activity,"download complete",Toast.LENGTH_SHORT).show()
                }

            }

        }

        val intentFilter= IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        activity?.registerReceiver(reciver,intentFilter)


    }
    fun getJsonResponse(url: String){
        val uri: Uri
        uri = Uri.parse(url)
        val path = uri.pathSegments
        println("path$path")
        val urlfinal = "https://www.instagram.com/"+path[0]+"/"+path[1]+"/?__a=1"
        println(urlfinal)
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, urlfinal,null,
            { response->
            Log.e("JSON", response.toString())


            },
            { error ->
            Log.e("Error", error.message.toString())
            })
        requestQueue.add(jsonObjectRequest)
    }

    fun getDownloadableUrl(url: String?){
        val uri: Uri
        uri = Uri.parse(url)
        val path = uri.pathSegments
        println("path$path")
        val urlfinal = "https://www.instagram.com/"+path[0]+"/"+path[1]+"/?__a=1"
        println(urlfinal)
       val requestQueue = Volley.newRequestQueue(context)

        val stringRequest=object :StringRequest(Method.GET,urlfinal,Response.Listener<String>
        {response ->
                var jsonresponse:String=response.toString()
                var video_url=""
                var pic_url=""

                try {
                    Log.d("jsonresponse", "getDownloadableUrl: $jsonresponse")
                    val json = JSONObject(jsonresponse)
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
                val json = JSONObject(jsonresponse)
                video_url = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                    .getString("video_url")
                downloadableurl=video_url
                Log.e("vid_url", video_url);
            }catch (e:Exception)
            {
                e.printStackTrace()
            }


            if (video_url!=""){
                    videoView.visibility =View.VISIBLE
                    imageview.visibility=View.GONE

                videoView.setVideoURI(Uri.parse(video_url))
                mediaController= MediaController(context)
                mediaController.setAnchorView(videoView)
                videoView.setMediaController(mediaController)

                videoView.setOnPreparedListener {
                    progress?.visibility = View.GONE
                   it.isLooping=false
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
                eturl?.setError("")

            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
//                headers["Content-Type"] = "application/json"
                headers["User-Agent"] = "Chrome/91.0.4472.77 Mobile"
                return headers
            }
        }
        requestQueue.add(stringRequest)

    }



    fun downloadFile(context: Context, fileName: String?, url: String?) {
        if (url != null) {
            Log.e("url",url)
        }

        val request=DownloadManager.Request(Uri.parse(url))
            .setTitle("Instagram ReelDownloader")
            .setDescription("Video is Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "Instagram Downloader/$fileName")

        val dm=context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        id=dm.enqueue(request)
    }


    fun hidekeyboard() {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken,
            InputMethodManager.SHOW_FORCED)
    }





}