package com.example.mediaplayer.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.CustomFragmentAdapter
import com.example.mediaplayer.databinding.LayoutPlayListBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MusicLIstBottomDialog: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogBind = DataBindingUtil.inflate<LayoutPlayListBottomDialogBinding>(inflater,
            R.layout.layout_play_list_bottom_dialog, null, false)
        val view = dialogBind.root
        dialogBind.musicListPager.adapter = CustomFragmentAdapter(
            childFragmentManager,
            mutableListOf(MusicListFragment(0), MusicListFragment(1))
        )
        dialogBind.musicTab.setupWithViewPager(dialogBind.musicListPager)
        return view
    }

    override fun onStart() {
        val window: Window? = dialog!!.window
        if (window != null) {
            // 一定要设置 Background, 如果不设置, Windows 属性设置无效
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dm = DisplayMetrics()
//            if (activity != null) {
//                val windowManager = activity!!.windowManager
//                if (windowManager != null) {
//                    windowManager.defaultDisplay.getMetrics(dm)
//                    val params: WindowManager.LayoutParams = window.getAttributes()
//                    params.gravity = Gravity.BOTTOM
//                    // 使用 ViewGroup.LayoutParams, 以便 Dialog 宽度充满整个屏幕
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT
//                    params.height = 500
//                    window.setAttributes(params)
//                }
//            }
        }
        val view = view

        view!!.post {
            var parent = view.parent as View
            val params =
                parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val mBottomSheetBehavior = behavior as BottomSheetBehavior<*>?
//            mBottomSheetBehavior?.setBottomSheetCallback(object :
//                BottomSheetBehavior.BottomSheetCallback() {
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                }
//
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
//
//                    }
//                    Log.d("zll", "onStateChanged: state = $newState")
//                }
//            })
            val display = activity!!.windowManager.defaultDisplay
            //设置高度
            val height = display.height / 2
            mBottomSheetBehavior?.peekHeight = height
            mBottomSheetBehavior?.isFitToContents = false
            mBottomSheetBehavior?.setExpandedOffset(height)
            parent.setBackgroundColor(Color.TRANSPARENT)
        }

        super.onStart()
    }


}