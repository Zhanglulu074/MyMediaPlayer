package com.example.mediaplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs

class CustomViewPager : ViewGroup {

    companion object {
        const val MAX_TRACKER_COLLECT_VOLOCITY = 100f
        const val TRACKER_COLLECT_UNITS = 10
        const val SMOOTH_SCROLL_TIME = 500
    }

    private var lastTouchX = 0F
    private var lastTouchY = 0F
    private var scroller: Scroller
    private var childWidth = 0
    private var currentChildIndex = 0
    private var lastX = 0
    private var lastY = 0
    private val tracker: VelocityTracker by lazy { VelocityTracker.obtain() }


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        scroller = Scroller(context)
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
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            width += child.measuredWidth
            childWidth = child.measuredWidth
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
        for (i in 0 until childCount) {
            child = getChildAt(i)
            child.layout(left, 0, left + child.measuredWidth, child.measuredHeight)
            left += child.measuredWidth
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercept = false
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = true
            }
            MotionEvent.ACTION_MOVE -> {
                val transitionX = ev.x - lastTouchX
                val transitionY = ev.y - lastTouchY
                intercept = abs(transitionX) > kotlin.math.abs(transitionY)
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
            }
            else -> {
            }
        }
        lastTouchX = ev!!.x
        lastTouchY = ev.y
        lastX = ev.x.toInt()
        lastY = ev.y.toInt()
        return intercept
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        tracker.addMovement(event)
        val currentX = event!!.x.toInt()
        val currentY = event.y.toInt()
        tracker.computeCurrentVelocity(TRACKER_COLLECT_UNITS, MAX_TRACKER_COLLECT_VOLOCITY)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = currentX - lastX
                scrollBy(-deltaX, 0)
            }
            MotionEvent.ACTION_UP -> {
                val distance = scrollX - childWidth * currentChildIndex
                if ((abs(distance) > childWidth / 2) || (abs(tracker.xVelocity) >= 35)) {
                    if (distance > 0) {
                        currentChildIndex++;
                    } else {
                        currentChildIndex--;
                    }
                }
                if (currentChildIndex < 0) {
                    currentChildIndex = 0
                }
                if (currentChildIndex > childCount - 1) {
                    currentChildIndex = childCount - 1
                }
                smoothScrollTo(childWidth * currentChildIndex, 0)
            }
        }
        lastX = currentX
        lastY = currentY
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    private fun smoothScrollTo(targetX: Int, targetY: Int) {
        scroller.startScroll(scrollX, scrollY, targetX - scrollX,
            targetY - scrollY, SMOOTH_SCROLL_TIME)
        invalidate()
    }
}