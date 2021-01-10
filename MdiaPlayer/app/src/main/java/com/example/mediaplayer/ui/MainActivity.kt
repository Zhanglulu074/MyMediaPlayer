package com.example.mediaplayer.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.EventLog
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.MusicAdapter
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.example.mediaplayer.events.MusicLoadEvent
import com.example.mediaplayer.service.MusicService
import com.example.mediaplayer.util.Util
import com.example.mediaplayer.viewmodel.CurrentMusicModel
import com.tbruyelle.rxpermissions2.RxPermissions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val TAG = "MainActivity"
    }

    private var needUnbind = false
    private lateinit var musicBinder: MusicService.MusicBinder
    private lateinit var seekBar: SeekBar
    private lateinit var bind: ActivityMainBinding
    private lateinit var rxPermissions: RxPermissions
    private lateinit var currentMusicModel: CurrentMusicModel

    private val musicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            needUnbind = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            needUnbind = true
            musicBinder = service as MusicService.MusicBinder
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bind.currentMusicModel = CurrentMusicModel.instance
        currentMusicModel = CurrentMusicModel.instance
        currentMusicModel.isPlaying.set(false)
        rxPermissions = RxPermissions(this)
        initViews()
        checkReadPermission(::initMusicService)
        EventBus.getDefault().register(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.music_pause_btn -> {
                if (currentMusicModel.isPlaying.get()!!) {
                    musicBinder.pause()
                    currentMusicModel.isPlaying.set(false)
                } else {
                    musicBinder.start()
                    currentMusicModel.isPlaying.set(true)
                }
            }
            R.id.music_last_btn -> {
                musicBinder.stop()
            }
            R.id.music_next_btn -> {
                if (needUnbind) {
                    unbindService(musicConnection)
                }
                needUnbind = false
            }
            else -> {
            }
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
        listOf(
            bind.musicPauseBtn,
            bind.musicLastBtn,
            bind.musicNextBtn
        ).forEach {
            it.setOnClickListener(this)
        }
        seekBar = bind.musicSeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (currentMusicModel.isChanged.get() == true) {
                    val currentTime = progress * currentMusicModel.musicLength / 1000
                    currentMusicModel.currentTime.set(Util.getTimeString(currentTime / 1000))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                currentMusicModel.isChanged.set(true)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                currentMusicModel.isChanged.set(false)
                musicBinder.seekToPosition(seekBar!!.progress.toFloat() / seekBar.max)
            }

        })
//        seekBar.max = 1000
    }

    private fun initMusicService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE)
    }

    private fun checkReadPermission(execution: () -> Unit) {
        val c = rxPermissions
            .request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    execution()
                }
            }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun loadMusicList(event: MusicLoadEvent) {
        bind.musicListView.adapter = MusicAdapter(event.musicList, this)
        bind.musicListView.layoutManager = LinearLayoutManager(this)
    }
}
