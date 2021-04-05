package com.example.mediaplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.mediaplayer.R

class RoundImageView: AppCompatImageView {

    private var path: Path? = null
    private var imageRadius: Float

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
        imageRadius = typedArray.getDimensionPixelOffset(R.styleable.RoundImageView_music_image_radius, 0).toFloat()
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (path == null) {
            path = Path()
            path?.addCircle(width / 2f, height / 2f, imageRadius, Path.Direction.CW)
        }
        canvas?.clipPath(path!!)
        super.onDraw(canvas)
    }
}