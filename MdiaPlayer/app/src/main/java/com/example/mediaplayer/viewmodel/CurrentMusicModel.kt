package com.example.mediaplayer.viewmodel

import androidx.databinding.ObservableField

class CurrentMusicModel private constructor() {

    companion object {
        val instance: CurrentMusicModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CurrentMusicModel()
        }
    }

    val musicName = ObservableField<String>()
    val musicLength = ObservableField<Int>()
    val currentProgress = ObservableField<Int>()
    val isChanged = ObservableField<Boolean>()
}