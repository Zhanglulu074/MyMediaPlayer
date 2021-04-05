package com.example.mediaplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import com.example.mediaplayer.R
import com.example.mediaplayer.viewmodel.CurrentMusicModel
import io.reactivex.Flowable.just
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class MusicPlayBackground : AppCompatImageView {

    companion object {
        const val ANIMATE_PERIOD = 2000
        const val ROTATE_PERIOD = 6000
        const val REFRESH_INTERVAL = 10
    }

    private var circleList: MutableList<MusicCircle> = mutableListOf()

    var circleNum: Int = 3
    var circleColor:Int = 0
    var miniRadius: Float = 0f
    var maxRadius: Float = 0f
    private val paint: Paint  = Paint()
    private var timer: Timer
    private var isPlaying: Boolean = false
    private var task: TimerTask
    private var curPeriod: Int = ANIMATE_PERIOD
    private var startTime: Long = 0L
    private var curDegree: Float = 0f
    private var curBallDegree: Double = 0.0
    var rotateImage: ImageView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        startTime = System.currentTimeMillis()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MusicPlayBackGround)
        circleColor = typedArray.getColor(R.styleable.MusicPlayBackGround_circleColor, Color.WHITE)
        miniRadius = typedArray.getDimensionPixelOffset(R.styleable.MusicPlayBackGround_minRadius, 300).toFloat()
        maxRadius = typedArray.getDimensionPixelOffset(R.styleable.MusicPlayBackGround_maxRadius, 500).toFloat()
        typedArray.recycle()
        paint.color = circleColor
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        timer = Timer()
        task = object : TimerTask() {
            var cnt = 0
            override fun run() {
                if (isPlaying && cnt % (curPeriod / REFRESH_INTERVAL / circleNum) == 0) {
                    addCircle()
                }
                if (circleList.isNotEmpty()) {
                    update()
                }
                cnt++
                cnt = if (cnt >= Integer.MAX_VALUE) 0 else cnt
            }
        }
        timer.schedule(task, 0, REFRESH_INTERVAL.toLong())
    }

    fun start() {
        startTime = System.currentTimeMillis()
        isPlaying = true
        curPeriod = ANIMATE_PERIOD
        val queue = ArrayBlockingQueue<String>(1000, true)
    }

    fun pause() {
        curPeriod = ANIMATE_PERIOD / 5
        isPlaying = false
    }

    fun resume() {
        curPeriod = ANIMATE_PERIOD
        isPlaying = true
    }

    fun addCircle() {
        curBallDegree += (2 * Math.PI / circleNum) % (2 * Math.PI)
        circleList.add(MusicCircle(miniRadius, 1f, curBallDegree, 6f))
    }

    fun update() {
        val iterator = circleList.iterator()
        while (iterator.hasNext()) {
            val circle = iterator.next()
            circle.circleRadius += (maxRadius - miniRadius) / (curPeriod / REFRESH_INTERVAL)
            circle.alpha = (maxRadius - circle.circleRadius).toFloat() / width * 2f
            circle.ballDegree += (2 * Math.PI / ROTATE_PERIOD * REFRESH_INTERVAL)
            circle.ballRadius += (20f - 6f) / (curPeriod / REFRESH_INTERVAL)
            if (circle.circleRadius > maxRadius) {
                iterator.remove()
            }
        }
        curDegree += (360f / ROTATE_PERIOD * REFRESH_INTERVAL).toFloat()
        if (curDegree >= 360f) {
            curDegree = 0f
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.translate(width / 2f, height / 2f)
        paint.alpha = 255
        paint.strokeWidth = 20f
        canvas?.drawCircle(0f, 0f, miniRadius, paint)
        paint.strokeWidth = 5f
        for (circle: MusicCircle in circleList) {
            paint.alpha = (circle.alpha * 255).toInt()
            canvas?.drawCircle(0f, 0f, circle.circleRadius.toFloat(), paint)
            paint.style = Paint.Style.FILL
            val ballX = Math.sin(circle.ballDegree) *  circle.circleRadius
            val ballY = Math.cos(circle.ballDegree) *  circle.circleRadius
            canvas?.drawCircle(ballX.toFloat(), ballY.toFloat(), circle.ballRadius, paint)
            paint.style = Paint.Style.STROKE
        }
        rotateImage?.rotation = curDegree
        super.onDraw(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer.cancel()
    }

    class MusicCircle(var circleRadius: Float = 300f, var alpha: Float = 1f, var ballDegree: Double, var ballRadius: Float = 0f)
}
