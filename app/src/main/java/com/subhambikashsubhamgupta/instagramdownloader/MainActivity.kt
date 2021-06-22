package com.subhambikashsubhamgupta.instagramdownloader

import android.os.Bundle
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


}