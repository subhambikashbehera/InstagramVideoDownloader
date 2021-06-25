package com.subhambikashsubhamgupta.instagramdownloader

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class fullvideo : AppCompatActivity() {

    lateinit var pause: ImageView
    lateinit var mediaController: MediaController
    private lateinit var videoView: VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullvideo)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        videoView=findViewById(R.id.videoViewfull)

        pause=findViewById(R.id.pause)

        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


       val videourl=intent.getStringExtra("video")

        videoView.setVideoURI(Uri.parse(videourl))

        videoView.setOnPreparedListener {
            it.isLooping=true
            it.start()
        }
       videoView.setOnClickListener {
           if (videoView.isPlaying)
           {
            videoView.pause()
               pause.visibility=View.VISIBLE
           }else
           {
               videoView.start()
               pause.visibility=View.GONE
           }
       }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }



}