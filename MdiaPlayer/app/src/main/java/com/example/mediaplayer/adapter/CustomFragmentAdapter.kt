package com.example.mediaplayer.adapter

import androidx.fragment.app.*

public class CustomFragmentAdapter : FragmentStatePagerAdapter {

    private lateinit var fragList: MutableList<Fragment>

    constructor(fm: FragmentManager, fragList: MutableList<Fragment>) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragList = fragList
    }


    override fun getItem(position: Int): Fragment {
        return fragList[position]
    }

    override fun getCount(): Int {
        return fragList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arrayListOf("本地音乐", "我喜欢的音乐")[position]
    }

}