package com.example.mediaplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.mediaplayer.R
import com.example.mediaplayer.events.MusicLoadEvent
import com.example.mediaplayer.repo.Repository
import com.example.mediaplayer.util.Util
import com.example.mediaplayer.viewmodel.CurrentMusicModel
import com.example.mediaplayer.viewmodel.MainPageViewModel
import com.example.mediaplayer.viewmodel.MusicModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.util.*


class MusicService : Service, MusicControl {
    constructor() : super()

    companion object {
        const val TAG = "MusicService"
    }

    private lateinit var sourcePath: String

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var task: TimerTask

    private lateinit var timer: Timer

    private var duration: Int = 1

    private var musicProgressBaseMax = 1f

    private val albumArtUri: Uri = Uri.parse("content://media/external/audio/albumart")

    private var curIdx: Int = -1

    private lateinit var currentMusicModel: CurrentMusicModel

    private lateinit var musicList: MutableList<MusicModel>

    inner class MusicBinder() : Binder(), MusicControl {
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

        override fun next() {
            this@MusicService.next()
        }

        override fun last() {
            this@MusicService.last()
        }

        override fun seekToPosition(fraction: Float) {
            this@MusicService.seekToPosition(fraction)
        }

        override fun switchTo(index: Int) {
            this@MusicService.switchTo(index)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return START_STICKY
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
        MusicCrtlCenter.instance.delegate = this
        initMediaPlayer()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        mediaPlayer.stop()
        mediaPlayer.reset()
        timer.cancel()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        return MusicBinder()
    }

    private fun initMediaPlayer() {
        musicProgressBaseMax = resources.getInteger(R.integer.music_progress_base_value).toFloat()
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            musicList = Repository.getAllMusicInDevice(this@MusicService)
            EventBus.getDefault().postSticky(MusicLoadEvent(musicList))
            MainPageViewModel.instance.localMusicList?.addAll(musicList)
            currentMusicModel = CurrentMusicModel.instance
            currentMusicModel.isChanged.set(false)
            mediaPlayer = MediaPlayer()
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
            Repository.writeIntoAlbumDb(this@MusicService, musicList)
        }
    }


    override fun start() {
        try {
            if (curIdx < 0) {
                curIdx = 0
                resetCurrentMusicIdx(currentMusicModel.currentIndex.get() ?: 0, curIdx)
                currentMusicModel.currentIndex.set(curIdx)
                playMusicWithIndex(curIdx)
            } else {
                mediaPlayer.start()
            }
        } catch (e: Exception) {
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

    override fun last() {
        curIdx--
        curIdx = if (curIdx < 0) musicList.size - 1 else curIdx
        resetCurrentMusicIdx(currentMusicModel.currentIndex.get() ?: 0, curIdx)
        currentMusicModel.currentIndex.set(curIdx)
        playMusicWithIndex(curIdx)
    }

    override fun next() {
        curIdx++
        curIdx = if (curIdx > musicList.size - 1) 0 else curIdx
        resetCurrentMusicIdx(currentMusicModel.currentIndex.get() ?: 0, curIdx)
        currentMusicModel.currentIndex.set(curIdx)
        playMusicWithIndex(curIdx)
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

    private fun playMusicWithIndex(index: Int) {
        val musicModel = musicList[index]
        currentMusicModel.musicName.set(musicModel.musicName.get())
        currentMusicModel.musicImageUri.set(musicModel.imageUri.get())
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(musicModel.musicPath.get())
        mediaPlayer.setOnPreparedListener {
            it.start()
            currentMusicModel.isPlaying.set(true)
            duration = mediaPlayer.duration
            currentMusicModel.musicLength = duration
            currentMusicModel.totalTime.set(Util.getTimeString(duration / 1000))
        }
        mediaPlayer.prepare()
    }

    override fun switchTo(index: Int) {
        curIdx = index
        resetCurrentMusicIdx(currentMusicModel.currentIndex.get() ?: 0, curIdx)
        currentMusicModel.currentIndex.set(index)
        playMusicWithIndex(index)
    }

    private fun resetCurrentMusicIdx(old: Int, cur: Int) {
        musicList[old].isPlaying.set(false)
        musicList[cur].isPlaying.set(true)
    }
}
