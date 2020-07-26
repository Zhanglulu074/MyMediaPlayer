package com.example.mediaplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.mediaplayer.model.MusicProcessModel
import java.io.File
import java.util.*

class MusicService : Service(), AbsMusicService{
    
    companion object {
        const val TAG = "MusicService"
    }

    private lateinit var sourcePath: String

    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }

    private lateinit var task: TimerTask

    private lateinit var timer: Timer

    private var duration: Int = 1

    inner class MusicBinder(): Binder(), AbsMusicService {
        override fun start() {
            this@MusicService.start()
        }

        override fun stop() {
            this@MusicService.stop()
        }

        override fun pause() {
            this@MusicService.pause()
        }

        override fun finish() {
            this@MusicService.finish()
        }

        override fun seekToPosition(fraction: Float) {
            this@MusicService.seekToPosition(fraction)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
        initMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        mediaPlayer.stop()
        mediaPlayer.release()
        timer.cancel()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        return MusicBinder()
    }

    fun initMediaPlayer() {
        sourcePath = "" + this.getExternalFilesDir(null) + "/Two Steps From Hell - Impossible.mp3"
        Log.d(TAG, "initMediaPlayer: $sourcePath")
        var file = File(sourcePath)
        Log.d(TAG, "initMediaPlayer: " + file.exists())
        mediaPlayer.setDataSource(sourcePath)
        mediaPlayer.prepare()
        duration = mediaPlayer.duration
        task = object : TimerTask() {
            override fun run() {
                val process = mediaPlayer.currentPosition
                val percent = process.toFloat() * 1000 / duration
                MusicProcessModel.instance.getData().postValue((percent.toInt()))
            }
        }
        timer = Timer()
        timer.schedule(task, 0, 300)
    }

    override fun start() {
        Log.d(TAG, "start: ")
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun stop() {
        Log.d(TAG, "stop: ")
    }

    override fun pause() {
        Log.d(TAG, "pause: ")
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun finish() {
        Log.d(TAG, "finish: ")
        mediaPlayer.stop()
        mediaPlayer.release()
        stopSelf()
    }

    override fun seekToPosition(fraction: Float) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.seekTo((fraction * duration).toInt())
        }
    }
}
