package com.example.mediaplayer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.example.mediaplayer.service.MusicService
import com.example.mediaplayer.viewmodel.CurrentMusicModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "MainActivity"
    }

    private var needUnbind = false
    private lateinit var musicBinder: MusicService.MusicBinder
    private lateinit var seekBar: SeekBar
    private lateinit var bind: ActivityMainBinding

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
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bind.currentMusicModel = CurrentMusicModel.instance
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
        listOf(bind.musicStartBtn, bind.musicPauseBtn, bind.musicStopBtn, bind.musicFinishBtn).forEach {
            it.setOnClickListener(this)
        }
        seekBar = bind.musicSeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                bind.currentMusicModel?.isChanged?.set(true)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                bind.currentMusicModel?.isChanged?.set(false)
                musicBinder.seekToPosition(seekBar!!.progress.toFloat() / seekBar.max)
            }

        })
        seekBar.max = 1000
    }

    private fun initMusicService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)
    }

    private fun checkReadPermission(execution: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            execution()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> initMusicService()
            else -> {
            }
        }
    }
}
