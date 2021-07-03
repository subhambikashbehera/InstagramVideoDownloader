package com.subhambikashsubhamgupta.instagramdownloader

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    var is_permission=false
    lateinit var downloadFragment: DownloadFragment

    lateinit var viewPager: ViewPager2
    lateinit var tablayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        viewPager = findViewById(R.id.viewpager1)
        tablayout = findViewById(R.id.tablayout)
        viewPager.adapter = PageAdapters(supportFragmentManager, lifecycle)
        downloadFragment = DownloadFragment()

        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Download"
                1 -> tab.text = "History"
            }

        }.attach()
        val clipBoardManager = applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardManager.addPrimaryClipChangedListener {
            val copiedString = clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
            Log.e("clip","hi"+copiedString)
            if (copiedString != null) {

                downloadFragment.pasteFromClip(copiedString)

            }
        }

        val uri = intent.data
        Log.e("uri", uri.toString())
        if (uri != null) {
            val path = uri.pathSegments
            println(path)
        }
        checkpermisson()

    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.shareapp->
            {
                try {

                    val intent1 = Intent(Intent.ACTION_SEND)
                    intent1.type = "text/plain"
                    intent1.putExtra(Intent.EXTRA_SUBJECT, "REEL DOWNLOADER")
                     val shareMessage="https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                    intent1.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(intent1, "share by"))

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "error occured", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.privecypolicies->{
               val intent=Intent(this,Privacypolicies::class.java)
                startActivity(intent)
            }
        }

        return true

    }


    fun checkpermisson(){

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),103)

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode==103 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"Permisssion is needed for Downloading and showing the videos",Toast.LENGTH_LONG).show()
            checkpermisson()
        }else
        {
            Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



}