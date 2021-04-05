package com.example.mediaplayer.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestOptions
import com.example.mediaplayer.R
import com.example.mediaplayer.application.GlideApp
import com.example.mediaplayer.databinding.ActivityVideoBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class VideoActivity : AppCompatActivity() {
    
    companion object {
        const val TAG = "VideoActivity"
    }

    private val sLocalVideoColumns = arrayOf(
        MediaStore.Video.Media._ID,  // 视频id
        MediaStore.Video.Media.DATA,  // 视频路径
        MediaStore.Video.Media.SIZE,  // 视频字节大小
        MediaStore.Video.Media.DISPLAY_NAME,  // 视频名称 xxx.mp4
        MediaStore.Video.Media.TITLE,  // 视频标题
        MediaStore.Video.Media.DATE_ADDED,  // 视频添加到MediaProvider的时间
        MediaStore.Video.Media.DATE_MODIFIED,  // 上次修改时间，该列用于内部MediaScanner扫描，外部不要修改
        MediaStore.Video.Media.MIME_TYPE,  // 视频类型 video/mp4
        MediaStore.Video.Media.DURATION,  // 视频时长
        MediaStore.Video.Media.ARTIST,  // 艺人名称
        MediaStore.Video.Media.ALBUM,  // 艺人专辑名称
        MediaStore.Video.Media.RESOLUTION,  // 视频分辨率 X x Y格式
        MediaStore.Video.Media.DESCRIPTION,  // 视频描述
        MediaStore.Video.Media.IS_PRIVATE,
        MediaStore.Video.Media.TAGS,
        MediaStore.Video.Media.CATEGORY,  // YouTube类别
        MediaStore.Video.Media.LANGUAGE,  // 视频使用语言
        MediaStore.Video.Media.LATITUDE,  // 拍下该视频时的纬度
        MediaStore.Video.Media.LONGITUDE,  // 拍下该视频时的经度
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.MINI_THUMB_MAGIC,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.BOOKMARK // 上次视频播放的位置
    )

    private lateinit var videoPlayer: VideoView
    private var lastVideoPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("zll", "onCreate: ")
        setContentView(R.layout.activity_video)
        val cursor: Cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            sLocalVideoColumns,
            null,
            null,
            null
        )!!
        cursor.moveToNext()
        val _id: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
        val filePath: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
        val title: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val mime_type: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
        val artist: String =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
        val albumId: Long =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
        val binding: ActivityVideoBinding = DataBindingUtil.setContentView(this, R.layout.activity_video)
        videoPlayer = binding.videoPlayer

        videoPlayer.setVideoPath(filePath)
        videoPlayer.start()
        binding.fullScreenButton.setOnClickListener {
            getScreenMessage()
        }
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .miniThumb(100)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .circleCrop()
            .transform(BlurTransformation(10), RoundedCornersTransformation(20, 0))
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            Log.d(TAG, "onCreate: thread1 = " + Thread.currentThread().id)
            val url = "http://guolin.tech/test.gif"
            val drawable = withContext(Dispatchers.IO) {
                val target: FutureTarget<Drawable> = Glide.with(applicationContext)
                    .load(url)
                    .apply(options)
                    .submit()
                Glide.with(applicationContext)
                    .load(url)
                    .apply(options)
                    .into(binding.imgGlide)
                Log.d(TAG, "onCreate: thread2 = " + Thread.currentThread().id)
                target.get()
            }
            binding.imgGlide.setImageDrawable(drawable)
            Log.d(TAG, "onCreate: thread3 = " + Thread.currentThread().id)
        }
        Log.d(TAG, "onCreate: thread4 = " + Thread.currentThread().id)
        binding.btnPlay.setOnClickListener {
            if (videoPlayer.isPlaying) {
                videoPlayer.pause()
            } else {
                videoPlayer.start()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        lastVideoPosition = videoPlayer.currentPosition
        videoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.seekTo(lastVideoPosition)
        videoPlayer.start()
    }

    fun getScreenMessage() {
        val cfg: Configuration = resources.configuration
        requestedOrientation = if (cfg.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    fun RequestOptions.miniThumb(size: Int): RequestOptions {
        return this.fitCenter()
            .override(100)
    }
}
