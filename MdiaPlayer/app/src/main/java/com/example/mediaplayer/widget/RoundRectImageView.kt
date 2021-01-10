package com.example.mediaplayer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.example.mediaplayer.R

class RoundRectImageView: androidx.appcompat.widget.AppCompatImageView {

    var imageRadius: Int = 0
    var drawPath: Path? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView)
        imageRadius = typedArray.getDimensionPixelOffset(R.styleable.RoundRectImageView_image_radius, 0)
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawPath == null) {
            val rectF=  RectF(0f, 0f, width.toFloat(), height.toFloat())
            drawPath = Path()
            drawPath?.addRoundRect(rectF, imageRadius.toFloat(), imageRadius.toFloat(), Path.Direction.CW)
        }
        canvas?.clipPath(drawPath!!)
        super.onDraw(canvas)
    }


}