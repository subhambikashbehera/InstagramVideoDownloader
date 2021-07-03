package com.subhambikashsubhamgupta.instagramdownloader

import android.app.DownloadManager
import android.content.*
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.io.File


class DownloadFragment : Fragment(), SendDataInterface{

    private var download: Button? = null
    private var share: Button? = null
    private var fromclip: Button? = null
    private var progress: ProgressBar? = null
    private var imgvidcard: MaterialCardView? = null
    private var eturl: EditText? = null
    lateinit var imageview:ImageView
    lateinit var mediaController: MediaController
    private lateinit var videoView: VideoView
    lateinit var generate:Button
    var downloadableurl=""
    var filename = ""
    var video_url = ""
    var pic_url = ""
    var isVideo = false
    var id:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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

        progress?.isIndeterminate = true
        share?.isEnabled = false

        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val url : String? = clipboard.text as String?
        if (url?.contains("www.instagram") == true){
            eturl?.setText(url)
            hidekeyboard()
            progress?.visibility = View.VISIBLE
            eturl?.error = null
            getDownloadableUrl(eturl?.text.toString())
        }
        else{
            progress?.visibility = View.GONE
            Toast.makeText(activity, "Invalid Link", Toast.LENGTH_LONG).show()
        }


        eturl?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.contains("www.instagram") == true){
                    hidekeyboard()
                    progress?.visibility = View.VISIBLE
                    eturl?.error = null
                    getDownloadableUrl(eturl?.text.toString())
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        try {
            val extras = activity?.intent?.extras
            val value1 = extras!!.getString(Intent.EXTRA_TEXT)
            if (value1 != null)
            {
                eturl?.setText(value1)
                hidekeyboard()
                checkpermission()
                if(eturl?.text.toString().isNotEmpty()){
                    progress?.visibility = View.VISIBLE
                    eturl?.error = null
                    getDownloadableUrl(eturl?.text.toString())
                } else
                    Toast.makeText(activity, "Enter Link Then Click Generate", Toast.LENGTH_LONG).show()
            }

        }catch (E:Exception)
        {
            E.printStackTrace()
        }



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
            //videoView.start()
            progress?.visibility = View.VISIBLE
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
                    share?.isEnabled = true
                    eturl?.setText(null)
                    progress?.visibility = View.GONE
                    share?.setOnClickListener {
                        activity?.applicationContext?.let { it1 -> share(it1,"" ) }
                    }
                }

            }

        }

        val intentFilter= IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        activity?.registerReceiver(reciver,intentFilter)


    }
    override fun fromclipBoard(string: String) {
        eturl?.setText(string)
    }
    fun pasteFromClip(string: String){
        eturl?.setText(string)
        Log.e("clipd",string)
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

                    try {
                        Log.d("jsonresponse", "getDownloadableUrl: $jsonresponse")
                        val json = JSONObject(jsonresponse)
                        val ur0 = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                            .getJSONArray("display_resources")
                        val jsonobject2 = ur0.getJSONObject(2)
                        pic_url = jsonobject2.getString("src")
                        Log.d("pic", pic_url);

                        imgvidcard?.visibility = View.VISIBLE

                        progress?.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        val json = JSONObject(jsonresponse)
                        video_url = json.getJSONObject("graphql").getJSONObject("shortcode_media")
                            .getString("video_url")
                        //downloadableurl = video_url

                        Log.e("vid_url", video_url);
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                   checkLinkForVorP()

                    if (video_url != "") {
                        download?.visibility=View.VISIBLE
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
                        Toast.makeText(activity, "Something Went Rong", Toast.LENGTH_SHORT).show()

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
    private fun showDialog(){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity?.applicationContext!!)
    }
    private fun checkLinkForVorP(){
        if (video_url.isNotEmpty()) {
            downloadableurl = video_url
            isVideo = true
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
            isVideo = false
            downloadableurl = pic_url
            videoView.visibility = View.GONE
            imageview.visibility = View.VISIBLE
            Glide
                .with(this)
                .load(pic_url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageview);
        }
    }

    private fun share(context: Context, link: String) {
        var extension = ".mp4"
        if (!isVideo)
            extension = ".jpg"
        val URI: Uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Instagram Downloader/$filename$extension"))
        Log.e("file",
            File(Environment.DIRECTORY_DOWNLOADS,"Instagram Downloader/$filename$extension").toString())

        val shareIntent = Intent()
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, URI)

        shareIntent.type = "*/*"
        activity?.startActivity(Intent.createChooser(shareIntent, "Share Video to.."))
    }



    fun downloadFile(context: Context, url: String?) {
        if (url != null) {
            Log.e("url",url)
        }
        var extension =""
        if (!isVideo)
            extension = ".jpg"
        else
            extension = ".mp4"


        //String name = filename.replaceAll("[^a-zA-Z0-9]", "");
        filename = (System.currentTimeMillis() / 1000).toString()
        val request=DownloadManager.Request(Uri.parse(url))
            .setTitle("Instagram ReelDownloader")
            .setDescription("Video is Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "Instagram Downloader/$filename$extension"
            )

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