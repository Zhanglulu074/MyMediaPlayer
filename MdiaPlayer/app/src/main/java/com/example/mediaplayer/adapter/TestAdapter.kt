package com.example.mediaplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.data.User

class TestAdapter(var userList: List<User>, var context: Context) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_test_list_item, parent, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        view.findViewById<TextView>(R.id.number_tv).text = userList[position].userId
        view.findViewById<TextView>(R.id.name_tv).text = userList[position].userName
    }
}