package com.mionix.baseapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mionix.baseapp.R

class MainMyPageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_my_page, container, false)
    }

    companion object {
        fun newInstance(): MainMyPageFragment {
            val args = Bundle()
            val fragment = MainMyPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}