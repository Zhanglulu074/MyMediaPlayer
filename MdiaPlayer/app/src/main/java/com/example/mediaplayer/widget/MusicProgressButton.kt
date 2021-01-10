package com.example.mediaplayer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import com.example.mediaplayer.R

class MusicProgressButton: androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    @SuppressLint("Recycle")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MusicProgressButton)
        process = typedArray.getFloat(R.styleable.MusicProgressButton_progress, 0f)
        baseMax = typedArray.getFloat(R.styleable.MusicProgressButton_max, 1f)
    }

    private var circleWidth = 0
    private var circleHeight = 0
    var process: Float = 0.0f
        set(value) {
            Log.d("zll", ": value $value")
            field = value
            invalidate()
        }

    var baseMax: Float = 0.0f
    private var paint: Paint = Paint()

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        drawable?.let {
            circleHeight = it.intrinsicWidth
            circleHeight = it.intrinsicHeight
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.scale(0.9f, 0.9f, width / 2f, height / 2f)
        super.onDraw(canvas)
        canvas?.let {
            if (circleHeight == 0 || circleWidth == 0) {
                drawable?.let {d ->
                    circleWidth = width
                    circleHeight = height
                }
            }
            Thread(object : Runnable {
                override fun run() {
                    Looper.prepare()
                    val subHandler = object : Handler() {
                        override fun handleMessage(msg: Message) {
                            super.handleMessage(msg)
                        }
                    }
                    Looper.loop()
                }
            })
            val handler: Handler = Handler()
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND;
            paint.strokeCap = Paint.Cap.ROUND;
            paint.strokeWidth = 6f
            paint.isAntiAlias = true
            paint.color = Color.GRAY
            it.drawCircle(circleWidth / 2f, circleHeight / 2f, circleHeight / 2f, paint)
            paint.color = Color.BLACK
            it.drawArc(0f, 0f, circleWidth.toFloat(), circleHeight.toFloat(), -90f, 360f *  process / baseMax, false, paint)
        }
    }


}