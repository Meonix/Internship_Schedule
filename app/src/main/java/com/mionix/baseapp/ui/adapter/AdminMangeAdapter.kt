package com.mionix.baseapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mionix.baseapp.R
import com.mionix.baseapp.model.UserModel
import kotlinx.android.synthetic.main.item_user_admin_mange.view.*

class AdminMangeAdapter(private val data:MutableList<UserModel>): RecyclerView.Adapter<AdminMangeAdapter.UserList>() {
    var onItemClick: ((userModel: UserModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminMangeAdapter.UserList {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_admin_mange, parent, false)
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
            itemView.itemUserAdmin_tvNameOfUser.text =  model.last_name + model.first_name +"("+model.nick_name+")"

            itemView.itemUserAdmin_tvPhone.text = "Phone :${model.phone ?:""}"
            itemView.itemUserAdmin_tvPosition.text = "Position:${model.postion ?: ""}"
            itemView.itemUserAdmin_btDeleteUser.setOnClickListener {
                onItemClick?.invoke(model)
            }
        }
    }
}