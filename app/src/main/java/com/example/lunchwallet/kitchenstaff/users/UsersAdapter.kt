package com.example.lunchwallet.kitchenstaff.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lunchwallet.R

class UsersAdapter(private val userList: ArrayList<ExistingUsers>): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {


    inner class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.user_name)
        val stack: TextView = itemView.findViewById(R.id.user_stack)
        val location: TextView = itemView.findViewById(R.id.user_location)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.users_rv, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.apply {
            name.text = userList[position].name
            stack.text = userList[position].stack
            location.text = userList[position].location
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}