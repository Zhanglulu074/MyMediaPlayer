package com.example.mediaplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mediaplayer.R
import com.example.mediaplayer.data.User
import com.example.mediaplayer.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind: ActivitySecondBinding =  DataBindingUtil.setContentView(this, R.layout.activity_second)
        var user: User = User("1", "Zhanglulu")
        bind.user = user
        user.userId = "2"
    }
}