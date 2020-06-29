package com.example.mediaplayer.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService : Service(), AbsMusicService{
    
    companion object {
        const val TAG = "MusicService"
    }

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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        return MusicBinder()
    }

    override fun start() {
        Log.d(TAG, "start: ")
    }

    override fun stop() {
        Log.d(TAG, "stop: ")
    }

    override fun pause() {
        Log.d(TAG, "pause: ")
    }

    override fun finish() {
        Log.d(TAG, "finish: ")
        stopSelf()
    }
}
