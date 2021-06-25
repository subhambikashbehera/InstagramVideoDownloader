package com.subhambikashsubhamgupta.instagramdownloader

import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject


class DownloadFragment : Fragment() {

    private var download: Button? = null
    private var share: Button? = null
    private var fromclip: Button? = null
    private var progress: ProgressBar? = null
    private var imgvidcard: MaterialCardView? = null
    private var mcard: MaterialCardView? = null
    private var eturl: EditText? = null
    lateinit var imageview:ImageView
    lateinit var mediaController: MediaController
    private lateinit var videoView: VideoView
    lateinit var generate:Button
    var downloadableurl=""
    var filename = ""
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
        imgvidcard=view.findViewById(R.id.m2card)
        mcard=view.findViewById(R.id.mcard)
        progress?.isIndeterminate = true










        generate.setOnClickListener {
            hidekeyboard()
            checkpermission()
            if(eturl?.text.toString().isNotEmpty()){
                progress?.visibility = View.VISIBLE
                eturl?.error = null
                getDownloadableUrl(eturl?.text.toString())
            } else
                Toast.makeText(activity, "Enter Link Then Click Generate", Toast.LENGTH_LONG).show()
            }

        download?.setOnClickListener {
            videoView.start()
            activity?.applicationContext?.let { downloadFile(it,downloadableurl) }
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
                    share?.setOnClickListener {
                        activity?.applicationContext?.let { it1 -> share(it1,"" ) }
                    }
                }

            }

        }

        val intentFilter= IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        activity?.registerReceiver(reciver,intentFilter)


    }
    fun getDownloadableUrl(url: String?){
        val uri: Uri
        uri = Uri.parse(url)
        val path = uri.pathSegments
        println(path[0])
        if (path[0].equals("p") or path[0].equals("reel")){
            println("path$path")
            val urlfinal = "https://www.instagram.com/" + path[0] + "/" + path[1] + "/?__a=1"
            println(urlfinal)
            val requestQueue = Volley.newRequestQueue(context)
            val stringRequest =
                object : StringRequest(Method.GET, urlfinal, Response.Listener<String>
                { response ->
                    var jsonresponse: String = response.toString()
                    var video_url = ""
                    var pic_url = ""
                    try {
                        Log.d("jsonresponse", "getDownloadableUrl: $jsonresponse")
                        val json = JSONObject(jsonresponse)
                        val ur0 = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                            .getJSONArray("display_resources")
                        val jsonobject2 = ur0.getJSONObject(2)
                        pic_url = jsonobject2.getString("src")
                        Log.d("pic", pic_url);

                        imgvidcard?.visibility = View.VISIBLE
                        mcard?.visibility = View.VISIBLE
                        progress?.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        val json = JSONObject(jsonresponse)
                        video_url = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                            .getString("video_url")
                        downloadableurl = video_url

                        Log.e("vid_url", video_url);
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (video_url != "") {
                        videoView.visibility = View.VISIBLE
                        imageview.visibility = View.GONE
                        videoView.setVideoURI(Uri.parse(video_url))
                        mediaController = MediaController(context)
                        mediaController.setAnchorView(videoView)
                        videoView.setMediaController(mediaController)
                        videoView.setOnPreparedListener {
                            progress?.visibility = View.GONE
                            it.isLooping = false
                            it.start()
                        }
                    } else {
                        videoView.visibility = View.GONE
                        imageview.visibility = View.VISIBLE
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
                        eturl?.setError("Something Went Wrong")

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
        else {
            Toast.makeText(activity, "Link Not Supported", Toast.LENGTH_LONG).show()
            eturl?.error = "Link Not Supported"
            progress?.visibility = View.GONE

        }
    }
    private fun share(context: Context, link: String) {
//        try {
//            var path = MediaStore.Images.Media.insertImage(
//                activity?.getContentResolver(),
//                arrImagePath.get(slidePager.getCurrentItem()), "Title", null
//            )
//        } catch (e1: FileNotFoundException) {
//            e1.printStackTrace()
//        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share")

        context.startActivity(Intent.createChooser(intent, "Share via"))
    }



    fun downloadFile(context: Context, url: String?) {
        if (url != null) {
            Log.e("url",url)
        }
        //String name = filename.replaceAll("[^a-zA-Z0-9]", "");
        filename = (System.currentTimeMillis() / 1000).toString()
        val request=DownloadManager.Request(Uri.parse(url))
            .setTitle("Instagram ReelDownloader")
            .setDescription("Video is Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "Instagram Downloader/$filename.mp4")

        val dm=context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        id=dm.enqueue(request)
    }


    fun hidekeyboard() {
        val inputManager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken,
            InputMethodManager.SHOW_FORCED)
    }


    fun checkpermission()
    {
        if (activity?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED)
        {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),102) }

        }
    }




}