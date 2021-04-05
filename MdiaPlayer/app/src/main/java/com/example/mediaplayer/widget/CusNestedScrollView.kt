package com.example.mediaplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat

class CusNestedScrollView: LinearLayout, NestedScrollingChild {

    private var mNestedScrollHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    private var mLastY = 0f

    private var mConsumed = IntArray(2)
    private var mOffsets = IntArray(2)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mNestedScrollHelper.isNestedScrollingEnabled = true
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mNestedScrollHelper.startNestedScroll(axes)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mNestedScrollHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action) {
                MotionEvent.ACTION_DOWN -> {
                    mLastY = it.rawY
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                }
                MotionEvent.ACTION_MOVE -> {
                    var dy = mLastY - it.rawY
                    mLastY = it.rawY
                    if (dispatchNestedPreScroll(0, dy.toInt(), mConsumed, mOffsets)) {
                        dy -= mConsumed[1].toFloat()
                    }
                    scrollBy(0, dy.toInt())
                }
                else -> {

                }
            }
        }
        return super.onTouchEvent(event)
    }

}