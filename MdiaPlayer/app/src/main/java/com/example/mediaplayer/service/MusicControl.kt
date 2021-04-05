package com.example.mediaplayer.service

interface MusicControl {
    fun start()
    fun stop()
    fun pause()
    fun finish()
    fun next()
    fun last()
    fun seekToPosition(fraction: Float)
    fun switchTo(index: Int)
}