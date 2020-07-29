package com.mionix.baseapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mionix.baseapp.ui.fragment.CurrentWorkingCalFragment

class MainHomeViewPagerAdapter(
    private val listTitle: MutableList<String>,
    manager: FragmentManager
) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (listTitle[position]) {
            "Working Calendar" -> CurrentWorkingCalFragment.newInstance()
            //"Register Calendar" -> RegisterCalFragment.newInstance()
           /* 5 -> UsefulInfoFragment.newInstance()*/
            else -> CurrentWorkingCalFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return listTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listTitle[position]
    }

}

