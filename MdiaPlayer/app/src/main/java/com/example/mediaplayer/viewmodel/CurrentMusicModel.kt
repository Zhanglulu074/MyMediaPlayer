package com.example.mediaplayer.viewmodel

import androidx.databinding.ObservableField

class CurrentMusicModel() {

    companion object {
        val instance: CurrentMusicModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CurrentMusicModel()
        }
    }

    val musicName = ObservableField<String>()
    var musicLength = 0
    val currentProgress = ObservableField<Int>()
    val isChanged = ObservableField<Boolean>()
    val isPlaying = ObservableField<Boolean>()
    val totalTime = ObservableField<String>()
    val currentTime = ObservableField<String>()

    init {
        currentTime.set("0:00")
    }
}