package com.mionix.baseapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.client.collection.LLRBNode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mionix.baseapp.R
import com.mionix.baseapp.model.UserModel
import com.mionix.baseapp.ui.activity.CurrentWorkingCalMothViewActivity
import com.mionix.baseapp.ui.activity.RegisterTimeWorkingActivity
import com.mionix.baseapp.ui.adapter.CurrentWorkingCalAdapter
import com.mionix.baseapp.utils.AppExecutors
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_register_time_working.*
import kotlinx.android.synthetic.main.fragment_current_working_cal.*
import kotlinx.android.synthetic.main.fragment_current_working_cal.btRegisterTimeWorking
import java.time.LocalDate
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CurrentWorkingCalFragment : Fragment() {
    private var mAuth = FirebaseAuth.getInstance()
    private var currentUser = mAuth.currentUser
    private var usersRef = FirebaseDatabase.getInstance().reference.child("Users")
    private lateinit var mUsersAdapter : CurrentWorkingCalAdapter
    private var listUser = mutableListOf<UserModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_working_cal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        checkCurrentUser()

    }

    private fun checkCurrentUser() {
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val position = dataSnapshot.child("position").getValue(String::class.java)
                if(position != "system_admin" && position != "admin"){
                    //User is intern
                    llCurrentWorkingCal.visibility = View.VISIBLE
                    btRegisterTimeWorking.visibility = View.VISIBLE
                    btOpenTime.visibility = View.INVISIBLE
                    currentUser?.uid?.let {
                        if (position != null) {
                            handleOnClick(it,position)
                        }
                    }
                }
                else if(position == "system_admin" || position == "admin"){
                    //User is admin or system admin
                    tvTopOfRv.visibility = View.VISIBLE
                    btOpenTime.visibility = View.VISIBLE
                    btRegisterTimeWorking.visibility = View.INVISIBLE
                    rvCurrentWorkingCal.visibility = View.VISIBLE
                    setupRecycleView()
                    currentUser?.uid?.let { handleOnClick(it,position) }
                }
            }
            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        usersRef.child(currentUser?.uid).addValueEventListener(eventListener)

        val eventListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val registerTime = dataSnapshot.child("Event").child("register_time").getValue(Int::class.java)
                val position = dataSnapshot.child("Users").child(currentUser?.uid).child("position").getValue(String::class.java)
                if(registerTime == 1 && (position != "system_admin" && position != "admin")){
                    //User is intern
                    if(btRegisterTimeWorking!=null){
                        btRegisterTimeWorking.visibility = View.VISIBLE
                        btOpenTime.visibility =View.INVISIBLE
                    }
                }
                else if(registerTime == 0 && (position == "system_admin" || position == "admin")){
                    if(btRegisterTimeWorking!=null){
                        btRegisterTimeWorking.visibility = View.INVISIBLE
                        btOpenTime.visibility = View.VISIBLE
                    }

                }
            }
            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        val eventRef = FirebaseDatabase.getInstance().reference
        eventRef.addValueEventListener(eventListener2)
    }

    private fun setupRecycleView() {
        if(rvCurrentWorkingCal != null){
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listUser.clear()
                    var thisPosition = ""
                    for (postSnapshot in dataSnapshot.children) {
                        val firstName = postSnapshot.child("first_name").getValue(String::class.java)
                        val lastName = postSnapshot.child("last_name").getValue(String::class.java)
                        val phone = postSnapshot.child("phone").getValue(String::class.java)
                        val position = postSnapshot.child("position").getValue(String::class.java)
                        val nickName = postSnapshot.child("nick_name").getValue(String::class.java)
                        val birthDay = postSnapshot.child("birth_day").getValue(String::class.java)
                        val sex = postSnapshot.child("sex").getValue(Int::class.java)
                        val uid = postSnapshot.child("uid").getValue(String::class.java)
                        if(position != "system_admin" && position != "admin"){
                            listUser.add(UserModel(firstName,lastName,nickName,birthDay,phone,position,sex,uid))
                        }
                        if(uid == currentUser?.uid){
                            if (position != null) {
                                thisPosition = position
                            }
                        }
                    }
                    mUsersAdapter = CurrentWorkingCalAdapter(listUser)
                    rvCurrentWorkingCal.adapter = mUsersAdapter
                    rvCurrentWorkingCal.layoutManager = LinearLayoutManager(context)
                    (rvCurrentWorkingCal.adapter as CurrentWorkingCalAdapter).notifyDataSetChanged()
                    mUsersAdapter.onItemClick={
                        llCurrentWorkingCal.visibility = View.VISIBLE
                        it.uid?.let {uid->
                            handleOnClick(uid,thisPosition)
                        }
//                        if(postSnapshot.child("uid").getValue(String::class.java)==it.uid){
//                            postSnapshot.ref.removeValue()
//                        }
                    }
                    mUsersAdapter.onBackgroundItemChange={
                        it.setTextColor(Color.RED)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError?) {

                }
            }
            usersRef.addValueEventListener(eventListener)
        }

    }

    private fun setupView() {
//        val eventListener: ValueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val position = dataSnapshot.child("position").getValue(String::class.java)
//                if(position != "system_admin" && position != "admin"){
//
//                }
//                else{
//
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError?) {
//
//            }
//        }
//        usersRef.child(currentUser?.uid.toString()).addValueEventListener(eventListener)
        val c = Calendar.getInstance()
        if(context!=null){
            when(c.get(Calendar.MONTH)+1){
                1 ->{
                    btJanuary.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                2->{
                    btFebruary.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                3->{
                    btMarch.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                4->{
                    btApril.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                5->{
                    btMay.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                6->{
                    btJune.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                7->{
                    btJuly.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                8->{
                    btAugust.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                9->{
                    btSeptember.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                10->{
                    btOctober.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                11->{
                    btNovember.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
                12->{
                    btDecember.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)
                }
            }
        }

    }

    private fun handleOnClick(uid:String,position:String) {
        btJanuary.onClickThrottled {
            openMothWorkingCalendar(1,uid,position)
        }
        btFebruary.onClickThrottled {
            openMothWorkingCalendar(2,uid,position)
        }
        btMarch.onClickThrottled {
            openMothWorkingCalendar(3,uid,position)
        }
        btApril.onClickThrottled {
            openMothWorkingCalendar(4,uid,position)
        }
        btMay.onClickThrottled {
            openMothWorkingCalendar(5,uid,position)
        }
        btJune.onClickThrottled {
            openMothWorkingCalendar(6,uid,position)
        }
        btJuly.onClickThrottled {
            openMothWorkingCalendar(7,uid,position)
        }
        btAugust.onClickThrottled {
            openMothWorkingCalendar(8,uid,position)
        }
        btSeptember.onClickThrottled {
            openMothWorkingCalendar(9,uid,position)
        }
        btOctober.onClickThrottled {
            openMothWorkingCalendar(10,uid,position)
        }
        btNovember.onClickThrottled {
            openMothWorkingCalendar(11,uid,position)
        }
        btDecember.onClickThrottled {
            openMothWorkingCalendar(12,uid,position)
        }
        btRegisterTimeWorking.onClickThrottled {
            val intent = Intent(context, RegisterTimeWorkingActivity::class.java)
            startActivity(intent)
        }
        btOpenTime.onClickThrottled {
            if(btOpenTime.text.toString() == "Open Registration Time") {
                val eventRef = FirebaseDatabase.getInstance().reference.child("Event")
                eventRef.child("register_time").setValue(1)
                btOpenTime.text = "Close Registration Time"
            }else{
                val eventRef = FirebaseDatabase.getInstance().reference.child("Event")
                eventRef.child("register_time").setValue(0)
                btOpenTime.text = "Open Registration Time"
            }

        }
    }
    private fun openMothWorkingCalendar(intMoth: Int,uid :String,position:String) {
        val intent = Intent(context,CurrentWorkingCalMothViewActivity::class.java)
        intent.putExtra(CurrentWorkingCalMothViewActivity.KEY_MOTH,intMoth)
        intent.putExtra(CurrentWorkingCalMothViewActivity.KEY_UID,uid)
        intent.putExtra(CurrentWorkingCalMothViewActivity.KEY_POSITION,position)
        startActivity(intent)
    }


    companion object {
        fun newInstance(): CurrentWorkingCalFragment {
            val args = Bundle()
            val fragment = CurrentWorkingCalFragment()
            fragment.arguments = args
            return fragment
        }
    }


}