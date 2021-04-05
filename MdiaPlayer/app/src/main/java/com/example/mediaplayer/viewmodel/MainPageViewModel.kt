package com.example.mediaplayer.viewmodel

import android.graphics.Color
import androidx.annotation.IntDef
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.mediaplayer.service.MusicCrtlCenter

class MainPageViewModel private constructor(){

    companion object {

        public const val CLICK_PLAY_MUSIC = 0
        const val CLICK_NEXT_MUSIC = 2
        const val CLICK_LAST_MUSIC = 3

        val instance: MainPageViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MainPageViewModel()
        }
    }

    var localMusicList: ObservableArrayList<MusicModel>? = ObservableArrayList()
    var favoriteMusicList: ObservableArrayList<MusicModel>? = ObservableArrayList()
    var currentMusicModel: CurrentMusicModel = CurrentMusicModel.instance
    var mainPageTextColor = ObservableField<Int>(Color.WHITE)
    var mainPageTextColorUncheck = ObservableField<Int>(Color.GRAY)

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(
        CLICK_PLAY_MUSIC,
        CLICK_NEXT_MUSIC,
        CLICK_LAST_MUSIC
    )
    private annotation class MusicClickEvent

    var additionalPlayEvent: (() -> Unit)? = null
    var additionalPauseEvent: (() -> Unit)? = null

    fun onCLick(@MusicClickEvent event: Int) {
        when (event) {
            CLICK_PLAY_MUSIC -> {
                if (currentMusicModel.isPlaying.get() == true) {
                    additionalPauseEvent?.let { it() }
                    MusicCrtlCenter.instance.pause()
                    currentMusicModel.isPlaying.set(false)
                } else {
                    additionalPlayEvent?.let { it() }
                    MusicCrtlCenter.instance.start()
                    currentMusicModel.isPlaying.set(true)
                }


            }
            CLICK_NEXT_MUSIC -> {
                MusicCrtlCenter.instance.next()

            }
            CLICK_LAST_MUSIC -> {
                MusicCrtlCenter.instance.last()
            }
            else -> {

            }
        }
    }
}