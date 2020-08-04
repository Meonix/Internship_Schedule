package com.mionix.baseapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mionix.baseapp.R
import com.mionix.baseapp.model.UserModel
import kotlinx.android.synthetic.main.item_users_current_working_cal.view.*

class CurrentWorkingCalAdapter (private val data:MutableList<UserModel>): RecyclerView.Adapter<CurrentWorkingCalAdapter.UserList>() {
    var onItemClick: ((userModel: UserModel) -> Unit)? = null
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
        @SuppressLint("SetTextI18n")
        fun onBindData(model : UserModel){
            itemView.usersCurrentWorkingCal_tvUserName.text =  model.last_name + model.first_name +"("+model.nick_name+")"
            itemView.setOnClickListener {
                onItemClick?.invoke(model)
            }
        }
    }
}