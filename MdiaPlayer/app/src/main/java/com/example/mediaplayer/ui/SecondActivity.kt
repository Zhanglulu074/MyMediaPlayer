package com.example.mediaplayer.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.CustomFragmentAdapter
import com.example.mediaplayer.component.DaggerAppComponent
import com.example.mediaplayer.databinding.ActivitySecondBinding
import com.example.mediaplayer.viewmodel.UserViewModel
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class SecondActivity() : AppCompatActivity() {

    @Inject
    lateinit var userMode: UserViewModel
    private val baiduUrl = "http://www.baidu.com"
    private val taobaoUrl = "http://ip.taobao.com/outGetIpInfo?ip=59.108.54.37"
    private val imageUrl =
        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3312430165,842994800&fm=26&gp=0.jpg"

    companion object {
        private const val TAG = "SecondActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind: ActivitySecondBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_second)
        DaggerAppComponent.builder().build().inject(this)
        bind.userMode = userMode
        userMode.userId.set("abc")
        userMode.userName.set("zhanglulu")
        var fragList: MutableList<Fragment> = mutableListOf()
        bind.fragmentViewPager.adapter = CustomFragmentAdapter(supportFragmentManager, fragList)
        Log.d(TAG, "onCreate: ")
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val res = async {
                val text: String = getText()
                userMode.userId.set(text)
                Log.d(TAG, "onCreate: threadId1 = " + Thread.currentThread().id)
                text
            }
        }
    }

    suspend fun getText(): String = withContext(Dispatchers.IO) {
        Log.d(TAG, "onCreate: threadId = " + Thread.currentThread().id)
        Thread.sleep(1000)
        "Hello World"
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    @Throws(Exception::class)
    fun read(inStream: InputStream): ByteArray? {
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        while (inStream.read(buffer).also { len = it } != -1) {
            outStream.write(buffer, 0, len)
        }
        inStream.close()
        return outStream.toByteArray()
    }
}