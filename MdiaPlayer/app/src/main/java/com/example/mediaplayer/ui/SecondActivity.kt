package com.example.mediaplayer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageLoader.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mediaplayer.R
import com.example.mediaplayer.cache.BitmapCache
import com.example.mediaplayer.component.DaggerAppComponent
import com.example.mediaplayer.data.GitHubRepo
import com.example.mediaplayer.databinding.ActivitySecondBinding
import com.example.mediaplayer.events.MessageEvent
import com.example.mediaplayer.networkinterface.GitHubClient
import com.example.mediaplayer.viewmodel.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class SecondActivity : AppCompatActivity() {

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
        bind.btAdd.setOnClickListener {
            EventBus.getDefault().postSticky(MessageEvent("测试第二次：黏性"))
            finish()

        }
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