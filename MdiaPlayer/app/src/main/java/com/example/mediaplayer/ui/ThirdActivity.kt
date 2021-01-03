package com.example.mediaplayer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.ActivitySecondBinding
import com.example.mediaplayer.databinding.ActivityThirdBinding
import com.example.mediaplayer.databinding.ActivityThirdBindingImpl
import com.example.mediaplayer.events.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ThirdActivity : AppCompatActivity() {

    lateinit var bind: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_third)

        bind.btEventRegister.setOnClickListener{
            EventBus.getDefault().register(this)
        }

        bind.btJump.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onMoonEvent(messageEvent: MessageEvent) {
        bind.tvEventReceiver.text = messageEvent.message
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}