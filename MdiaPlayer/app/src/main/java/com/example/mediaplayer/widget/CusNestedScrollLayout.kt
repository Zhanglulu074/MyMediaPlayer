package com.example.mediaplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class CusNestedScrollLayout: LinearLayout {

    private var totalScroll = 0;

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun startNestedScroll(axes: Int): Boolean {
        return true;
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        totalScroll += dy
        if (totalScroll <= 300) {
            scrollBy(0, dy)
            consumed?.set(1, dy)
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}