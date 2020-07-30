package com.mionix.baseapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.activity.CreateAccountActivity
import com.mionix.baseapp.utils.AppExecutors
import kotlinx.android.synthetic.main.fragment_admin_mange.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AdminMangeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_mange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val spinnerCreateAccountOption = arrayOf("- Select One -","Admin","Intern")
        val arrayAdapterCreateAccountOption = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,spinnerCreateAccountOption)
        spCreateAccountOption.adapter = arrayAdapterCreateAccountOption
        spCreateAccountOption.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                if(spCreateAccountOption.selectedItem.toString()=="Intern"){
                    val intent = Intent(context, CreateAccountActivity::class.java)
                    startActivity(intent)
                }
                if(spCreateAccountOption.selectedItem.toString()=="Admin"){
//                           Log.d("DUY","ADqwe")
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    override fun onStart() {
        spCreateAccountOption.setSelection(0)
        super.onStart()
    }

    companion object {
        fun newInstance(): AdminMangeFragment {
            val args = Bundle()
            val fragment = AdminMangeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}