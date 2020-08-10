package com.mionix.baseapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.mionix.baseapp.R
import com.mionix.baseapp.model.UserModel
import com.mionix.baseapp.ui.activity.CreateAccountActivity
import com.mionix.baseapp.ui.adapter.AdminMangeAdapter
import com.mionix.baseapp.utils.AppExecutors
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_admin_mange.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class AdminMangeFragment : Fragment() {

    private lateinit var mUsersAdapter : AdminMangeAdapter
    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    private var listUser = mutableListOf<UserModel>()
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
        setupRecycleView()
    }


    private fun setupRecycleView() {
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUser.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val firstName = postSnapshot.child("first_name").getValue(String::class.java)
                    val lastName = postSnapshot.child("last_name").getValue(String::class.java)
                    val phone = postSnapshot.child("phone").getValue(String::class.java)
                    val position = postSnapshot.child("position").getValue(String::class.java)
                    val nickName = postSnapshot.child("nick_name").getValue(String::class.java)
                    val birthDay = postSnapshot.child("birth_day").getValue(String::class.java)
                    val sex = postSnapshot.child("sex").getValue(Int::class.java)
                    val uid = postSnapshot.child("uid").getValue(String::class.java)
                    if(position != "system_admin"){
                        listUser.add(UserModel(firstName,lastName,nickName,birthDay,phone,position,sex,uid))
                    }
                }
                mUsersAdapter = AdminMangeAdapter(listUser)
                rvListUserFragmentAdminMange.adapter = mUsersAdapter
                rvListUserFragmentAdminMange.layoutManager = LinearLayoutManager(context)
                (rvListUserFragmentAdminMange.adapter as AdminMangeAdapter).notifyDataSetChanged()
                mUsersAdapter.onItemClick={
                    for (postSnapshot in dataSnapshot.children) {
                        if(postSnapshot.child("uid").getValue(String::class.java)==it.uid){
                            postSnapshot.ref.removeValue()
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        usersRef.addValueEventListener(eventListener)
    }

    private fun setupView() {
        val spinnerCreateAccountOption = arrayOf("- Add -","Admin","Intern")
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