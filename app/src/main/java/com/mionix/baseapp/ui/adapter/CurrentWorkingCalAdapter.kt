package com.mionix.baseapp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.mionix.baseapp.R
import com.mionix.baseapp.model.UserModel
import kotlinx.android.synthetic.main.fragment_current_working_cal.*
import kotlinx.android.synthetic.main.item_users_current_working_cal.view.*
import java.util.*

class CurrentWorkingCalAdapter (private val data:MutableList<UserModel>): RecyclerView.Adapter<CurrentWorkingCalAdapter.UserList>() {
    var onItemClick: ((userModel: UserModel) -> Unit)? = null
    var onBackgroundItemChange: ((textView: TextView) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserList {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_users_current_working_cal, parent, false)
        return UserList(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UserList, position: Int) {
       holder.onBindData(data[position])
    }
    inner class UserList(itemView: View) : RecyclerView.ViewHolder(itemView){
        var usersRef = FirebaseDatabase.getInstance().reference.child("Users")
        @SuppressLint("SetTextI18n")
        fun onBindData(model : UserModel){
            itemView.usersCurrentWorkingCal_tvUserName.text =  model.last_name + model.first_name +"("+model.nick_name+")"
            itemView.setOnClickListener {
                onItemClick?.invoke(model)
            }
            val eventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val calendarDay =  Calendar.getInstance()
                    val fullTime = dataSnapshot.child("working_time").child((calendarDay.get(Calendar.MONTH)+2).toString()).child("full_time").getValue(String::class.java)
                    val morningTime = dataSnapshot.child("working_time").child((calendarDay.get(Calendar.MONTH)+2).toString()).child("morning_time").getValue(String::class.java)
                    val afternoonTime = dataSnapshot.child("working_time").child((calendarDay.get(Calendar.MONTH)+2).toString()).child("afternoon_time").getValue(String::class.java)
                    if(fullTime.isNullOrEmpty() && morningTime.isNullOrEmpty() && afternoonTime.isNullOrEmpty()){
                        onBackgroundItemChange?.invoke(itemView.usersCurrentWorkingCal_tvUserName)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError?) {

                }
            }
            usersRef.child(model.uid).addValueEventListener(eventListener)
        }
    }
}