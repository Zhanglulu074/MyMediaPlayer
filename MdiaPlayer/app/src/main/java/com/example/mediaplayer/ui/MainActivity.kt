package com.example.mediaplayer.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.CustomFragmentAdapter
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.example.mediaplayer.service.MusicCrtlCenter
import com.example.mediaplayer.service.MusicService
import com.example.mediaplayer.util.Util
import com.example.mediaplayer.viewmodel.CurrentMusicModel
import com.example.mediaplayer.viewmodel.MainPageViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var seekBar: SeekBar
    private lateinit var bind: ActivityMainBinding
    private lateinit var rxPermissions: RxPermissions
    private lateinit var currentMusicModel: CurrentMusicModel
    private lateinit var co: CompositeDisposable


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bind.currentMusicModel = CurrentMusicModel.instance
        bind.mainActivityModel = MainPageViewModel.instance
        bind.musicListPager.adapter = CustomFragmentAdapter(
            supportFragmentManager,
            mutableListOf(MusicListFragment(0), MusicListFragment(1))
        )
        currentMusicModel = CurrentMusicModel.instance
        currentMusicModel.isPlaying.set(false)
        rxPermissions = RxPermissions(this)
        co = CompositeDisposable()
        bind.musicTab.setupWithViewPager(bind.musicListPager)
        initViews()
        checkReadPermissionThen(::initMusicService)
    }


    private fun initViews() {
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
                MusicCrtlCenter.instance.seekToPosition(seekBar!!.progress.toFloat() / seekBar.max)
            }

        })
    }

    private fun initMusicService() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
    }

    private fun checkReadPermissionThen(execution: () -> Unit) {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    execution()
                }
            }.addTo(co)
    }

    override fun onDestroy() {
        super.onDestroy()
        co.clear()
    }


    fun Disposable.addTo(co: CompositeDisposable) {
        co.add(this)
    }

}
