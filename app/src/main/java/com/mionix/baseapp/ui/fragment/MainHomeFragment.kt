package com.mionix.baseapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.adapter.MainHomeViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_main_home.*
import kotlinx.android.synthetic.main.fragment_main_my_page.*
import java.util.*


class MainHomeFragment : Fragment() {
    private var fragmentNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

    }



    private fun setupView() {
        fragmentNames.addAll(
            mutableListOf(
                "Working Calendar"//, "Register Calendar"
            )
        )
        setupViewPager()

    }

    private fun setupViewPager() {
        val mAdapter = MainHomeViewPagerAdapter(fragmentNames, childFragmentManager)
        mPager.adapter = mAdapter
        mPager.offscreenPageLimit = 5
//        if (!(context as MainActivity).dialog.isShowing!!) {
//            mPager.currentItem = 0
//        } else {
//            if ((context as MainActivity).deepLink != null) {
//                var patchLevel1 = (context as MainActivity).deepLink?.pathSegments?.get(0)
//                if (patchLevel1 == MainActivity.GUIDANCELIST) {
//                    mPager.currentItem = 1
//                }
//            }
//        }
        tabLayout.setupWithViewPager(mPager)
    }

    companion object {
        fun newInstance(): MainHomeFragment {
            val args = Bundle()
            val fragment = MainHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}