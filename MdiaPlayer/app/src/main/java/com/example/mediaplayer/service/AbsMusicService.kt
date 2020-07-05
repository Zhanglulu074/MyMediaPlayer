package com.example.mediaplayer.service

interface AbsMusicService {
    fun start()
    fun stop()
    fun pause()
    fun finish()
    fun seekToPosition(fraction: Float)
}