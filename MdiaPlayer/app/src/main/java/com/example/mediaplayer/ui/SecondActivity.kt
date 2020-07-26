package com.example.mediaplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mediaplayer.R
import com.example.mediaplayer.component.AppComponent
import com.example.mediaplayer.component.DaggerAppComponent
import com.example.mediaplayer.data.User
import com.example.mediaplayer.databinding.ActivitySecondBinding
import com.example.mediaplayer.viewmodel.UserViewModel
import javax.inject.Inject

class SecondActivity : AppCompatActivity() {

    @Inject
    lateinit var userMode: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind: ActivitySecondBinding =  DataBindingUtil.setContentView(this, R.layout.activity_second)
        DaggerAppComponent.builder().build().inject(this)
        bind.userMode = userMode
        userMode.userId.set("abc")
        userMode.userName.set("zhanglulu")
    }
}