package com.subhambikashsubhamgupta.instagramdownloader

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView

class fullvideo : AppCompatActivity() {


    lateinit var mediaController: MediaController
    private lateinit var videouri: Uri
    private lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullvideo)
        videoView=findViewById(R.id.videoViewfull)
       val videourl=intent.getStringExtra("video")
        videoView.setVideoURI(Uri.parse(videourl))
        mediaController= MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.setOnPreparedListener {
            it.isLooping=true
            it.start()
        }
    }

}