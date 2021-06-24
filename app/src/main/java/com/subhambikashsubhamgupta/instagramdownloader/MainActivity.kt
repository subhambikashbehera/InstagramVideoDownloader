package com.subhambikashsubhamgupta.instagramdownloader

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager2
    lateinit var tablayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        viewPager = findViewById(R.id.viewpager1)
        tablayout = findViewById(R.id.tablayout)
        viewPager.adapter = PageAdapters(supportFragmentManager, lifecycle)

        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Download"
                1 -> tab.text = "History"
            }

        }.attach()

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

}