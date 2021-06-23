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
  /*  {"graphql":{"shortcode_media":{"__typename":"GraphVideo","id":"2600647472862755467","shortcode":"CQXWxkGAHqL","dimensions":{"height":640,"width":640},"gating_info":null,"fact_check_overall_rating":null,"fact_check_information":null,"sensitivity_friction_info":null,"sharing_friction_info":{"should_have_sharing_friction":false,"bloks_app_url":null},"media_overlay_info":null,"media_preview":"ACoqulqq3JkZdsYJJ6+w\/wDr1NyeB1NQXF4LZgi\/Mec+mcVLKWvoVraKWIFmUjPTkZrNk8zcWYEZ\/T0rR\/tJmOPl+nIH86hF0LhthAXcD0NKzWpd09DZhn81A\/qOfr3p+6qVipWLB65NWaogcvykMO1QXNushMq8A5yO+e5\/+t+NXNtZ2oTeWAgOCeuPT\/69NrsJPuUZNLKnqDmoks2V8A\/TH9asR6gQAHG7B4I69KmtLoSybANu4dT6jn9RU+8X7pfRSqgHqBS0802nYgnzWBeHdK5P0FbwrnLgku3sT\/OrRJVRsY9jU0T+VIrf3W\/rz+lVh1qcfeH1H86AOjY803NNbrTaQz\/\/2Q==","display_url":"https:\/\/instagram.fbbi1-1.fna.fbcdn.net\/v\/t51.2885-15\/e35\/204445584_837690373529605_2535045865203704370_n.jpg?tp=1&_nc_ht=instagram.fbbi1-1.fna.fbcdn.net&_nc_cat=1&_nc_ohc=XyfRrbmwxiQAX-zlw64&edm=AABBvjUBAAAA&ccb=7-4&oh=e6322e383eee25bed9d0c187ebb52db2&oe=60D48059&_nc_sid=83d603","display_resources":[{"src":"https:\/\/instagram.fbbi1-1.fna.fbcdn.net\/v\/t51.2885-15\/e35\/204445584_837690373529605_2535045865203704370_n.jpg?tp=1&_nc_ht=instagram.fbbi1-1.fna.fbcdn.net&_nc_cat=1&_nc_ohc=XyfRrbmwxiQAX-zlw64&edm=AABBvjUBAAAA&ccb=7-4&oh=e6322e383eee25bed9d0c187ebb52db2&oe=60D48059&_nc_sid=83d603","config_width":640,"config_height":640},{"src":"https:\/\/instagram.fbbi1-1.fna.fbcdn.net\/v\/t51.2885-15\/e35\/204445584_837690373529605_2535045865203704370_n.jpg?tp=1&_nc_ht=instagram.fbbi1-1.fna.fbcdn.net&_nc_cat=1&_nc_ohc=XyfRrbmwxiQAX-zlw64&edm=AABBvjUBAAAA&ccb=7-4&oh=e6322e383eee25bed9d0c187ebb52db2&oe=60D48059&_nc_sid=83d603","config_width":750,"config_height":750},{"src":"https:\/\/instagram.fbbi1-1.fna.fbcdn.net\/v\/t51.2885-15\/e35\/204445584_837690373529605_2535045865203704370_n.jpg?tp=1&_nc_ht=instagram.fbbi1-1.fna.fbcdn.net&_nc_cat=1&_nc_ohc=XyfRrbmwxiQAX-zlw64&edm=AABBvjUBAAAA&ccb=7-4&oh=e6322e383eee25bed9d0c187ebb52db2&oe=60D48059&_nc_sid=83d603","config_width":1080,"config_height":1080}],"accessibility_caption":null,"dash_info":{"is_dash_eligible":false,"video_dash_manifest":null,"number_of_qualities":0},"has_audio":true,"video_url":"https:\/\/instagram.fbbi1-1.fna.fbcdn.net\/v\/t50.2886-16\/203164362_1224622161299748_147716886690496890_n.mp4?_nc_ht=instagram.fbbi1-1.fna.fbcdn.net&_nc_cat=100&_nc_ohc=HwOdaksA2TYAX8T4sp_&edm=AABBvjUBAAAA&ccb=7-4&oe=60D429A9&oh=7024e633f2676f89f461c532c9aba71e&_nc_sid=83d603","video_view_count":23787,"video_play_count":null,"is_video":true,"tracking_token":"eyJ2ZXJzaW9uIjo1LCJwYXlsb2FkIjp7ImlzX2FuYWx5dGljc190cmFja2VkIjp0cnVlLCJ1dWlkIjoiYTUyNzBkZWQ0ZWJkNDVhMmFkNGU5ZGJlOTljN2NiNzcyNjAwNjQ3NDcyODYyNzU1NDY3In0sInNpZ25hdHVyZSI6IiJ9","upcoming_event_info":null,"edge_media_to_tagged_user":{"edges":[]},"edge_media_to_caption":{"edges":[{"node":{"text":"FOLLOW @insta__life21FOR MORE VIDEOS \n➖➖➖➖➖➖➖➖➖➖➖➖➖✔️\n- 🅾 ®️ 👉❤️LIKE ND COMMENT 📝 ➖➖➖➖➖➖➖➖➖➖➖➖➖✔️\n- 🅾 ®️ 👉SHARE ND REPOST 🔁 ➖➖➖➖➖➖➖➖➖➖➖➖➖ ✔️\n- 🅾 ®️ 👉✌️KEEP FOLLOWING ✅ ➖➖➖➖➖➖➖➖➖➖➖➖➖ ✔️\n- 🅾 ®️👉TURN ON POST NOTIFICATIONS ➖➖➖➖➖➖➖➖➖➖➖➖➖✔️\n- 🅾 ®️👉Follow For Status Video ✅\n➖➖➖➖➖➖➖➖➖➖➖➖➖\n#tiktokbrandstatus 🕺🏻 #tiktokgirlsonly #unmarriedcouple #coupleschool #aems #romanticcouplestatus  #aemsprajapati😎 #itxaman__ #bollywoodstatusvideo #loveu4evar #tiktokloverspoint #statusloverharu #blackstatuslover 🖤 #hearttouching 💘  #lovefeelings\n#whatsappstatussong \n#lovesongs #wafa #truelovefeelings 😘 #whatsappstatus #insta_nonstop_fun #hindisongs #ishq  #hearttouchingquotes \n#lovestatus 💝 #statusvideo #insta__life21"}}]},"can_see_insights_as_brand":false,"caption_is_edited":false,"has_ranked_comments":false,"like_and_view_counts_disabled":false,"edge_media_to_parent_comment":{"count":3,"page_info
*/
    }