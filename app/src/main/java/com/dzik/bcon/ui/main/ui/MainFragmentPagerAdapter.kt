package com.dzik.bcon.ui.main.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainFragmentPagerAdapter(fm: FragmentManager?,
                               objects: MutableList<Fragment>) : FragmentPagerAdapter(fm) {

    private val fragments = objects

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }


}