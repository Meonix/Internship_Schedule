package com.mionix.baseapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mionix.baseapp.R
import com.mionix.baseapp.model.DateModel
import com.mionix.baseapp.model.local.Preferences
import com.mionix.baseapp.ui.activity.CreateAccountActivity
import com.mionix.baseapp.ui.custom.CustomCalendar
import com.mionix.baseapp.utils.AppExecutors
import com.mionix.baseapp.utils.KeyboardUtils
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.fragment_main_my_page.*
import org.koin.android.ext.android.inject
import java.util.HashMap

class MainMyPageFragment : Fragment() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
//    private var userModel: UserModel? = null
    private var currentUser = mAuth.currentUser
    private var userInfo = HashMap<String, Any>()
    private var dateModelStart = DateModel()
    private var dateModelEnd = DateModel()
    private var mStartDateFrom = ""
    private val mPreferences by inject<Preferences>()
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
            if(edNewPassword.text.toString() != ""){
                mAuth.currentUser?.updatePassword(edNewPassword.text.toString())
                mPreferences.setLocalPassword(edNewPassword.text.toString())
            }
            userInfo["first_name"]= etFirstName.text.toString()
            userInfo["last_name"]= etLastName.text.toString()

            userInfo["phone"] = edPhone.text.toString()
            var sex = 2
            when(spSex.selectedItemPosition){
                0 -> sex = 1
                1 -> sex = 2
                2 -> sex = 3
            }
            userInfo["sex"]  = sex
            userInfo["birth_day"] = tvBirthday.text.toString()
            if(llPositionMyFragment.isVisible){
                userInfo["position"] = spPosition.selectedItem.toString()
            }
            usersRef.child(currentUser?.uid.toString()).updateChildren(userInfo).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context,"Update Profile Successful..",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,it.exception.toString(),Toast.LENGTH_SHORT).show()
                }
            }


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

    override fun onStart() {
        super.onStart()

    }
    private fun setUpView(
        birthDay: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        nickName: String?,
        phone: String?,
        position: String?,
        sex: Int?
    ) {
        if(firstName!= null){
            etFirstName.setText(firstName)
        }
        if(lastName!= null){
            etLastName.setText(lastName)
        }
//        if(userModel?.nick_name!= null){
//            etNickName.setText(userModel?.nick_name)
//        }
        if(birthDay != null){
            tvBirthday.text = birthDay
        }

        if(phone!=null){
            edPhone.setText(phone)
        }
        if(context!= null){
            val spinnerLeft = arrayOf("Male","FeMale","Other")
            val arrayAdapterLeft = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,spinnerLeft)
            spSex.adapter = arrayAdapterLeft
            if(position != "admin" && position != "system_admin"){
                val spinnerPosition = arrayOf("BE (Node JS)",
                    "BE (.NET)","BE (PHP)","BE (Python)","BE (RoR)","Unity","FE (HTML&CSS)",
                    "FE (React)","FE (Angular)","FE (Vue)","Mobile (iOS)","Mobile (Android)","IT Director")
                val arrayAdapterPosition = ArrayAdapter(context!!,R.layout.support_simple_spinner_dropdown_item,spinnerPosition)
                spPosition.adapter = arrayAdapterPosition
            }
            else{
                llPositionMyFragment.visibility = View.GONE
            }


        }

        if(position!=null){
            var internPostion = 2
            when(position){
                "BE (Node JS)" -> internPostion = 0
                "BE (.NET)" -> internPostion = 1
                "BE (PHP)" -> internPostion = 2
                "BE (Python)" -> internPostion = 3
                "BE (RoR)" -> internPostion = 4
                "Unity" -> internPostion = 5
                "FE (HTML&CSS)" -> internPostion = 6
                "FE (React)" -> internPostion = 7
                "FE (Angular)" -> internPostion = 8
                "FE (Vue)" -> internPostion = 9
                "Mobile (iOS)" -> internPostion = 10
                "Mobile (Android)" -> internPostion = 11
                "IT Director" -> internPostion = 12
                else -> internPostion = 0
            }
            spPosition.setSelection(internPostion)

        }
        if(sex!=null){

            when(sex){
                1->spSex.setSelection(0)
                2->spSex.setSelection(1)
                3->spSex.setSelection(2)
                else->{
                    spSex.setSelection(2)
                }
            }
        }

    }

    private fun setUpData() {
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val birthDay = dataSnapshot.child("birth_day").getValue(String::class.java)
                    val email = dataSnapshot.child("email").getValue(String::class.java)
                    val firstName = dataSnapshot.child("first_name").getValue(String::class.java)
                    val lastName = dataSnapshot.child("last_name").getValue(String::class.java)
                    val nickName    =  dataSnapshot.child("nick_name").getValue(String::class.java)
                    val phone = dataSnapshot.child("phone").getValue(String::class.java)
                    val position = dataSnapshot.child("position").getValue(String::class.java)
                    val sex = dataSnapshot.child("sex").getValue(Int::class.java)
                    setUpView(birthDay,email,firstName,lastName,nickName,phone,position,sex)
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        usersRef.child(currentUser?.uid.toString()).addValueEventListener(eventListener)

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