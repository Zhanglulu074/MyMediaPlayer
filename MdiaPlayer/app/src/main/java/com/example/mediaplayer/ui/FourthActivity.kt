package com.example.mediaplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.TestAdapter
import com.example.mediaplayer.data.User

class FourthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        val userList = mutableListOf<User>()
        for (i in 1..100) {
            userList.add(User("1", "lulu"));
        }
        val listView = findViewById<RecyclerView>(R.id.test_list)
        listView.adapter = TestAdapter(userList, this)
        listView.layoutManager = LinearLayoutManager(this)
    }
}