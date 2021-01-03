package com.example.mediaplayer.service

import android.app.Service
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.example.mediaplayer.R
import com.example.mediaplayer.util.Util
import com.example.mediaplayer.viewmodel.CurrentMusicModel
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

    private var musicProgressBaseMax = 1f

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

    private fun initMediaPlayer() {
        musicProgressBaseMax = resources.getInteger(R.integer.music_progress_base_value).toFloat()
        val contentResolver = contentResolver
        val audioColumns = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.MIME_TYPE
        )
        val cursor: Cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            audioColumns,
            null,
            null,
            null
        )!!
        cursor.moveToNext()
        val _id: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
        val filePath: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
        val title: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val mime_type: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
        Log.d(TAG, "zll initMediaPlayer: _id=$_id")
        Log.d(TAG, "zll initMediaPlayer: title=$title")
        Log.d(TAG, "zll initMediaPlayer: filePath=$filePath")
        Log.d(TAG, "zll initMediaPlayer: mime_type=$mime_type")
        sourcePath = "" + this.getExternalFilesDir(null) + "/Two Steps From Hell - Impossible.mp3"
        sourcePath = filePath
        Log.d(TAG, "initMediaPlayer: $sourcePath")
        val file = File(sourcePath)
        val currentMusicModel = CurrentMusicModel.instance
        currentMusicModel.musicName.set(file.name.substring(0, file.name.lastIndexOf(".")))
        currentMusicModel.isChanged.set(false)
        Log.d(TAG, "initMediaPlayer: " + file.exists())
        mediaPlayer.setDataSource(sourcePath)
        mediaPlayer.prepare()
        duration = mediaPlayer.duration
        currentMusicModel.totalTime.set(Util.getTimeString(duration / 1000))
        currentMusicModel.musicLength = duration
        task = object : TimerTask() {
            override fun run() {
                val process = mediaPlayer.currentPosition
                currentMusicModel.currentTime.set(Util.getTimeString(process / 1000))
                val percent = process.toFloat() * musicProgressBaseMax / duration
                if (!currentMusicModel.isChanged.get()!!) {
                    currentMusicModel.currentProgress.set(percent.toInt())
                }
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
