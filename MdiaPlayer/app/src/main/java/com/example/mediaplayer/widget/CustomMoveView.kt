package com.example.mediaplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

class CustomMoveView : androidx.appcompat.widget.AppCompatTextView {

    private var lastX: Float = 0F
    private var lastY: Float = 0F

    private lateinit var scroller1: Scroller

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        scroller1 = Scroller(context)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val currentX = event!!.x;
        val currentY = event.y;
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val offsetX = (currentX - lastX).toInt()
                val offsetY = (currentY - lastY).toInt()
                (parent as View).scrollBy(-offsetX, -offsetY)
            }
            MotionEvent.ACTION_DOWN -> {
                lastX = currentX
                lastY = currentY
            }
            else -> {
            }
        }
        return true
    }
}