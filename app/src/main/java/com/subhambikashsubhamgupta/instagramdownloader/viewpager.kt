package com.subhambikashsubhamgupta.instagramdownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class viewpager : Fragment() {


    lateinit var tabLayout: TabLayout
    lateinit var viewpager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tabLayout=view.findViewById(R.id.tablayout)
        viewpager=view.findViewById(R.id.viewpager1)

        val pagerAdapter= activity?.let { PageAdapters(it.supportFragmentManager) }


        viewpager.adapter=pagerAdapter
        tabLayout.setupWithViewPager(viewpager)

        pagerAdapter?.addfragmemt(HistoryFragment(),"Histroy")
        pagerAdapter?.addfragmemt(DownloadFragment(),"Download")


    }

}