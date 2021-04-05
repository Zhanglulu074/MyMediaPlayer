package com.example.mediaplayer.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageLoader.getImageListener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mediaplayer.R
import com.example.mediaplayer.adapter.CustomFragmentAdapter
import com.example.mediaplayer.cache.BitmapCache
import com.example.mediaplayer.data.LyricBean
import com.example.mediaplayer.databinding.ActivityMusicPlayBinding
import com.example.mediaplayer.databinding.LayoutMusicListFragmentBinding
import com.example.mediaplayer.databinding.LayoutPlayListBottomDialogBinding
import com.example.mediaplayer.service.MusicCrtlCenter
import com.example.mediaplayer.service.MusicService
import com.example.mediaplayer.util.LyricUtil
import com.example.mediaplayer.util.Util
import com.example.mediaplayer.viewmodel.CurrentMusicModel
import com.example.mediaplayer.viewmodel.MainPageViewModel
import com.example.mediaplayer.widget.MusicPlayBackground
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*


class MusicPlayActivity : BaseFullScreenActivity() {

    companion object {
        const val TAG = "MusicPlayActivity"
    }

    private lateinit var seekBar: SeekBar
    private lateinit var currentMusicModel: CurrentMusicModel
    private lateinit var bind: ActivityMusicPlayBinding
    private var showBackground: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        startService(Intent(this, MusicService::class.java))
        bind = DataBindingUtil.setContentView<ActivityMusicPlayBinding>(this, R.layout.activity_music_play)
        bind.currentMusicModel = CurrentMusicModel.instance
        bind.mainActivityModel = MainPageViewModel.instance
        bind.musicPlayView.rotateImage = bind.musicRotateImage
        currentMusicModel = CurrentMusicModel.instance
        seekBar = bind.musicSeekBar
        val normThumb = resources.getDrawable(R.drawable.thumb_normal, theme)
        val pressThumb = resources.getDrawable(R.drawable.thumb_press, theme)
        seekBar.thumb = normThumb
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (currentMusicModel.isChanged.get() == true) {
                    val currentTime = progress * currentMusicModel.musicLength / 1000
                    currentMusicModel.currentTime.set(Util.getTimeString(currentTime / 1000))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                currentMusicModel.isChanged.set(true)
                seekBar?.thumb = pressThumb
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                currentMusicModel.isChanged.set(false)
                seekBar?.thumb = normThumb
                MusicCrtlCenter.instance.seekToPosition(seekBar!!.progress.toFloat() / seekBar.max)
            }
        })
        bind.musicPlayView.setOnClickListener {
            showBackground = !showBackground
            animateToShowBackground(showBackground)
        }
        bind.lyricView.setOnClickListener {
            showBackground = !showBackground
            animateToShowBackground(showBackground)
        }
        val file = File(Environment.getExternalStorageDirectory(), "testlyrics.lrc")
        var lyricList: List<LyricBean> = LyricUtil.parseLyricFile(file)
        bind.lyricView.lyricList = lyricList
        bind.musicPlayView.visibility = View.VISIBLE
        bind.musicRotateImage.visibility = View.VISIBLE
        bind.lyricView.visibility = View.INVISIBLE
        bind.musicListBtn.setOnClickListener {
            val dialog: MusicLIstBottomDialog =
                MusicLIstBottomDialog()
//            val dialogBind = DataBindingUtil.inflate<LayoutPlayListBottomDialogBinding>(LayoutInflater.from(this),
//                R.layout.layout_play_list_bottom_dialog, null, false)
//            val view = dialogBind.root
//            dialog.setContentView(view)
//
//            dialogBind.musicListPager.adapter = CustomFragmentAdapter(
//                getSupportFragmentManager(),
//                mutableListOf(MusicListFragment(0), MusicListFragment(1))
//            )
//            dialogBind.musicTab.setupWithViewPager(dialogBind.musicListPager)
//            try {
//                // hack bg color of the BottomSheetDialog
//                val parent = view.parent as ViewGroup
//                parent.setBackgroundResource(android.R.color.transparent)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            val behavior = BottomSheetBehavior.from(view.parent as View)
//            val wm = this.windowManager
//            val height = wm.defaultDisplay.height
//            behavior.peekHeight = (height * 0.4).toInt()
            dialog.show(supportFragmentManager, "test")
        }

    }

    fun animateToShowBackground(showBackground: Boolean) {
        val animator = if (showBackground) ValueAnimator.ofFloat( 0f, 1f) else ValueAnimator.ofFloat(1f, 0f)
        animator.addUpdateListener {
            Log.d(TAG, "animateToShowBackground: ${it.animatedValue as Float}")
            bind.musicPlayView.alpha = it.animatedValue as Float
            bind.musicRotateImage.alpha = it.animatedValue as Float
            bind.lyricView.alpha = 1f - it.animatedValue as Float
        }
        animator.addListener(object: Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                bind.musicPlayView.visibility = if (showBackground) View.VISIBLE else View.INVISIBLE
                bind.musicRotateImage.visibility = if (showBackground) View.VISIBLE else View.INVISIBLE
                bind.lyricView.visibility = if (showBackground) View.INVISIBLE else View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                bind.musicPlayView.visibility = View.VISIBLE
                bind.musicRotateImage.visibility = View.VISIBLE
                bind.lyricView.visibility = View.VISIBLE
            }
        })
        animator.duration = 500
        animator.interpolator = LinearInterpolator()
        animator.start()
    }
}