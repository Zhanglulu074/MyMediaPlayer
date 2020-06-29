package com.example.mediaplayer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mediaplayer.R
import com.example.mediaplayer.service.MusicService

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "MainActivity"
    }

    private var needUnbind = false
    private lateinit var musicBinder: MusicService.MusicBinder
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var stopBtn: Button
    private lateinit var finishBtn: Button
    private lateinit var testBtn: Button

    private val musicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            needUnbind = false
            Log.d(TAG, "onServiceDisconnected: ")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            needUnbind = true
            musicBinder = service as MusicService.MusicBinder
            Log.d(TAG, "onServiceConnected: ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        checkReadPermission(::initMusicService)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.music_start_btn -> {
                musicBinder.start()
            }
            R.id.music_pause_btn -> {
                musicBinder.pause()
            }
            R.id.music_stop_btn -> {
                musicBinder.stop()
            }
            R.id.music_finish_btn -> {
                if (needUnbind) {
                    unbindService(musicConnection)
                }
                needUnbind = false
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needUnbind) {
            Log.d(TAG, "onDestroy: needUnbind")
            unbindService(musicConnection)
        }
    }

    private fun initViews() {
        startBtn = findViewById(R.id.music_start_btn)
        pauseBtn = findViewById(R.id.music_pause_btn)
        stopBtn = findViewById(R.id.music_stop_btn)
        finishBtn = findViewById(R.id.music_finish_btn)
        testBtn = findViewById(R.id.music_test_btn)

        startBtn.setOnClickListener(this)
        pauseBtn.setOnClickListener(this)
        stopBtn.setOnClickListener(this)
        finishBtn.setOnClickListener(this)
        testBtn.setOnClickListener(this)
    }

    private fun initMusicService() {
        val intent: Intent = Intent(this,MusicService::class.java)
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)
    }

    private fun checkReadPermission(execution: ()-> Unit) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            execution()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> initMusicService()
            else -> {}
        }
    }
}
