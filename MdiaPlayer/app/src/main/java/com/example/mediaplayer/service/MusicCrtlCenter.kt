package com.example.mediaplayer.service

class MusicCrtlCenter : MusicControl{

    companion object {
        public val instance: MusicCrtlCenter by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicCrtlCenter()
        }
    }

    var delegate: MusicControl? = null

    override fun start() {
        delegate?.start()
    }

    override fun stop() {
        delegate?.stop()
    }

    override fun pause() {
        delegate?.pause()
    }

    override fun finish() {
        delegate?.finish()
    }

    override fun next() {
        delegate?.next()
    }

    override fun last() {
        delegate?.last()
    }

    override fun seekToPosition(fraction: Float) {
        delegate?.seekToPosition(fraction)
    }

    override fun switchTo(index: Int) {
        delegate?.switchTo(index)
    }
}