package com.example.mediaplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class CustomViewPager : ViewGroup {

    private var lastTouchX = 0F
    private var lastTouchY = 0F

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var width = 0;
        for (i in 0..childCount) {
            val child = getChildAt(i)
            width += child.measuredWidth
        }
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, getChildAt(0).measuredHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, heightSize)
        } else if (heightMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getChildAt(0).measuredWidth, getChildAt(0).measuredHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = l
        var child: View
        for (i in 0..childCount) {
            child = getChildAt(i)
            child.layout(left, 0, left + child.measuredWidth, child.measuredHeight)
            left += child.measuredWidth
        }
    }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        var intercept = false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
            }
            MotionEvent.ACTION_MOVE -> {
                val transitionX = event.x - lastTouchX
                val transitionY = event.y - lastTouchY
                intercept = kotlin.math.abs(transitionX) > kotlin.math.abs(transitionY)
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
            }
            else -> {}
        }
        lastTouchX = event!!.x
        lastTouchY = event.y
        return intercept
    }

}