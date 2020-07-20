package com.mionix.baseapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mionix.baseapp.R
import com.mionix.baseapp.model.DateModel
import com.mionix.baseapp.model.UserModel
import com.mionix.baseapp.ui.activity.CreateAccountActivity
import com.mionix.baseapp.ui.custom.CustomCalendar
import com.mionix.baseapp.utils.KeyboardUtils
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.fragment_main_my_page.*
import kotlinx.android.synthetic.main.fragment_main_my_page.btCreateAccount
import java.util.HashMap

class MainMyPageFragment : Fragment() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    private var userModel: UserModel? = null
    private var currentUser = mAuth.currentUser
    private var userInfo = HashMap<String, Any>()
    private var dateModelStart = DateModel()
    private var dateModelEnd = DateModel()
    private var mStartDateFrom = ""
    private val mCustomCalendar = CustomCalendar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
        setUpActionKeyBoard(rlFragmentMyPage)
        setupEventClick()

    }

    private fun setupEventClick() {
        btChange.onClickThrottled {
            userInfo["name"]= etName.text.toString()
            userInfo["email"] = edMail.text.toString()
            userInfo["phone"] = edPhone.text.toString()
            var sex = 2
            when(spSex.selectedItemPosition){
                0 -> sex = 1
                1 -> sex = 2
                2 -> sex = 3
            }
            userInfo["sex"]  = sex
            userInfo["birth_day"] = tvBirthday.text.toString()
            usersRef.child(currentUser?.uid.toString()).updateChildren(userInfo)
        }
        tvBirthday.setOnClickListener {
            /* if(birthday!="null"){
                 mCustomCalendar.setupDate(birthday.substring(8,10).toInt(),(birthday.substring(5,7).toInt()-1),birthday.substring(0,4).toInt(),datePickerEditUser)
             }*/
            llPickerDate.visibility = View.VISIBLE
            btChange.visibility = View.GONE

            tvPickedDate.setOnClickListener {
                dateModelStart = mCustomCalendar.startDate(datePickerEditUser,dateModelEnd)
                mStartDateFrom = String.format("%02d",(dateModelStart.month+1)).plus("/")
                    .plus(String.format("%02d",dateModelStart.day)).plus("/")
                    .plus(dateModelStart.year.toString())


                tvBirthday.text = mStartDateFrom
                btChange.visibility = View.VISIBLE
                llPickerDate.visibility = View.GONE
            }
//            tvNullBirthDay.setOnClickListener {
//                llPickerDate.visibility = View.GONE
//                btChange.visibility = View.VISIBLE
//                tvBirthday.text = ""
//            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpActionKeyBoard(view: View){
        // Set up touch listener for non-text box views to hide keyboard.
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                activity?.let { KeyboardUtils.hideSoftKeyboard(it) }
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setUpActionKeyBoard(innerView)
            }
        }
    }
    private fun setUpView() {
        if(userModel?.name!= null){
            etName.setText(userModel?.name)
        }
        if(userModel?.email != null){
            edMail.setText(userModel?.email)
        }
        if(userModel?.birth_day != null){
            tvBirthday.text = userModel?.birth_day
        }

        if(userModel?.phone!=null){
            edPhone.setText(userModel?.phone)
        }
        if(userModel?.postion!=null){
            tvInternPosition.text = userModel?.postion
        }
        if(context!= null){
            val spinnerLeft = arrayOf("Nam","Nữ","Khác")
            val arrayAdapterLeft = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,spinnerLeft)
            spSex.adapter = arrayAdapterLeft
        }
        if(userModel?.sex!=null){

            when(userModel?.sex){
                1->spSex.setSelection(0)
                2->spSex.setSelection(1)
                3->spSex.setSelection(2)
                else->{
                    spSex.setSelection(2)
                }
            }
            if(userModel?.postion == "admin"){
                btCreateAccount.visibility = View.VISIBLE
                btCreateAccount.onClickThrottled{
                    val intent = Intent(context, CreateAccountActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    private fun setUpData() {
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val birthDay = dataSnapshot.child("birth_day").getValue(String::class.java)
                    val email = dataSnapshot.child("email").getValue(String::class.java)
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    val phone = dataSnapshot.child("phone").getValue(String::class.java)
                    val position = dataSnapshot.child("position").getValue(String::class.java)
                    val sex = dataSnapshot.child("sex").getValue(Int::class.java)
                    userModel = UserModel(birthDay,email,name,phone,position,sex)
                    setUpView()
            }

            override fun onCancelled(databaseError: DatabaseError?) {}
        }
        usersRef.child(currentUser?.uid.toString()).addListenerForSingleValueEvent(eventListener)

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