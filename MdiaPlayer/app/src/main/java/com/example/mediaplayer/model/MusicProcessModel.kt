package com.example.mediaplayer.model

import androidx.lifecycle.MutableLiveData

class MusicProcessModel private constructor(){

    private val processData: MutableLiveData<Int> by lazy { MutableLiveData<Int>()}
    companion object {
        val instace: MusicProcessModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicProcessModel()
        }
    }

    fun getData() : MutableLiveData<Int> {
        return processData
    }
}