package com.subhambikashsubhamgupta.instagramdownloader

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapters(fm:FragmentManager): FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val mfragmentlist= arrayListOf<Fragment>()
    val mfragmenttittlelist= arrayListOf<String>()

    override fun getCount(): Int {
        return mfragmentlist.size
    }

    override fun getItem(position: Int): Fragment {
        return mfragmentlist[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
    return mfragmenttittlelist[position]
    }

    fun addfragmemt(fragmet:Fragment,tittle:String)
    {
        mfragmentlist.add(fragmet)
        mfragmenttittlelist.add(tittle)
    }



}