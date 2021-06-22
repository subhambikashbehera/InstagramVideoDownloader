package com.subhambikashsubhamgupta.instagramdownloader

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapters(fm:FragmentManager,lc:Lifecycle): FragmentStateAdapter(fm,lc)
{
    override fun getItemCount(): Int {
        return  2
    }
    override fun createFragment(position: Int): Fragment {
        when(position)
        {
            0->{return DownloadFragment()}
            1->{return HistoryFragment()}
            else->{return DownloadFragment()}
        }
    }
}