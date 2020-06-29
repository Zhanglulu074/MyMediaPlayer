package com.example.mediaplayer.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.mediaplayer.R

class CustomTitleBar : RelativeLayout{

    private lateinit var leftImage: ImageView

    private lateinit var titleText: TextView

    private lateinit var rightImage: ImageView

    private var leftDrawable: Drawable?

    private var rightDrawable: Drawable?

    private var titleString: String?

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar)
        leftDrawable = typedArray?.getDrawable(R.styleable.CustomTitleBar_title_bar_left_src)
        rightDrawable = typedArray?.getDrawable(R.styleable.CustomTitleBar_title_bar_right_src)
        titleString = typedArray?.getString(R.styleable.CustomTitleBar_title_bar_title)
        initView(context!!)
        typedArray?.recycle()
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.custom_title_bar, this, true)
        leftImage = findViewById(R.id.title_bar_left_image)
        titleText = findViewById(R.id.title_bar_title)
        rightImage = findViewById(R.id.title_bar_right_image)
        leftImage.setImageDrawable(leftDrawable)
        rightImage.setImageDrawable(rightDrawable)
        titleText.text = titleString
    }

    fun setLeftClickListener(listener: OnClickListener) {
        leftImage.setOnClickListener(listener)
    }

    fun setRightClickListener(listener: OnClickListener) {
        rightImage.setOnClickListener(listener)
    }

    fun setTitle(title: String) {
        titleText.text = title
    }
}