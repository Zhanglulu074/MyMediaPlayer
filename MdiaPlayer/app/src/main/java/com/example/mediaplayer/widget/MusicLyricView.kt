package com.example.mediaplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller
import androidx.palette.graphics.Palette
import com.example.mediaplayer.data.LyricBean

class MusicLyricView: View {

    var lyricList: List<LyricBean> = mutableListOf()

    var curPercent: Float = 0f

    var curIdx = 0;

    var totalTime: Long = 179280

    private var bPaint: Paint = Paint()
    private var hPaint: Paint = Paint()

    var frontColor: Int = Color.WHITE
    set(value) {
        field = value
        hPaint.color = frontColor
        invalidate()
    }
    var backColor: Int = Color.GRAY
    set(value) {
        field = value
        bPaint.color = value
        invalidate()
    }
    var rowSpace: Float = 180f
    var textSize: Float = 60f



    private lateinit var scroller: Scroller
    private var startY: Float = 0f


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        bPaint.color = Color.GRAY
        bPaint.alpha = 255
        hPaint.color = frontColor
        bPaint.textAlign = Paint.Align.CENTER
        hPaint.textAlign = Paint.Align.CENTER
        bPaint.textSize = textSize
        hPaint.isFakeBoldText = true
        hPaint.textSize = textSize * 1.1f
        scroller = Scroller(context)
    }

    fun setPercent(percent: Float) {
        curPercent = percent;
        val newIdx = calculateCurIdx(curPercent)
        if (curIdx != newIdx) {
            curIdx = newIdx
            val scrollTarget = height / 2f
            smoothScrollYTo(rowSpace.toInt() * curIdx)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val startY = height / 2f
        for (i in lyricList.indices) {
            val bean = lyricList[i]
            if (startY + rowSpace * i - scrollY < 0) {
                continue
            }
            if (startY + rowSpace * i - scrollY > height) {
                break
            }
            if (i == curIdx) {
                canvas?.drawText(bean.lyric, width.toFloat() / 2, startY + rowSpace * i, hPaint)
            } else {
                canvas?.drawText(bean.lyric, width.toFloat() / 2, startY + rowSpace * i, bPaint)
            }
        }

    }

    private fun calculateCurIdx(percent: Float): Int {
        var res = 0;
        var curTime: Long = (totalTime * percent).toLong()
        for (i in lyricList.indices) {
            val bean = lyricList[i]
            if (bean.lyric != "" && bean.startTime <= curTime && bean.endTime >= curTime) {
                res = i
                break
            }
        }
        return res;
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    private fun smoothScrollYTo(targetY: Int) {
        scroller.startScroll(scrollX, scrollY, 0, targetY - scrollY, 1500)
        invalidate()
    }
}