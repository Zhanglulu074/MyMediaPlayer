package com.example.mediaplayer.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mediaplayer.R
import com.example.mediaplayer.viewmodel.MainPageViewModel
import com.example.mediaplayer.widget.MusicLyricView
import com.example.mediaplayer.widget.MusicPlayBackground
import com.example.mediaplayer.widget.MusicProgressButton
import jp.wasabeef.glide.transformations.BlurTransformation
import java.text.DecimalFormat


class Util {
    companion object {
        public fun getTimeString(seconds: Int): String {
            if (seconds > 86400) {
                return "24:59:59"
            }
            val timeFormat = DecimalFormat("00")
            val sb = StringBuilder()
            var remain = seconds
            val hours = seconds / 3600
            remain %= 3600
            val mins = remain / 60
            remain %= 60
            sb.append(mins).append(":").append(timeFormat.format(remain))
            if (hours > 0) {
                sb.insert(0, hours)
            }
            return sb.toString()
        }

        @JvmStatic
        @BindingAdapter("progress")
        public fun setProgress(musicProgressButton: MusicProgressButton, progress: Int) {
            musicProgressButton.process = progress.toFloat()
        }

        @JvmStatic
        @BindingAdapter("imageUri")
        public fun setImageUri(imageView: ImageView, imageUri: Uri?) {
            if (imageUri == null) {
                imageView.setImageResource(R.drawable.default_music_3)
                return
            }
            try {
                val pfd: ParcelFileDescriptor? =
                    imageView.context.contentResolver.openFileDescriptor(imageUri, "r")
                if (pfd != null) {
                    val fd = pfd.fileDescriptor
                    val bm = BitmapFactory.decodeFileDescriptor(fd);
                    imageView.setImageBitmap(bm)
                } else {
                    imageView.setImageDrawable(imageView.context.resources.getDrawable(R.drawable.default_music_3))
                }
            } catch (e: Exception) {
                imageView.setImageDrawable(imageView.context.resources.getDrawable(R.drawable.default_music_3))
            }
        }

        @JvmStatic
        @BindingAdapter("isPlaying")
        public fun setIsPlaying(playView: MusicPlayBackground, isPlaying: Boolean) {
            if (isPlaying) {
                playView.start()
            } else {
                playView.pause()
            }
        }

        @JvmStatic
        @BindingAdapter("musicBigImage")
        public fun setMusicBigImage(bigImage: ImageView, imageUri: Uri?) {
            val options: RequestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(BlurTransformation(25, 8))
            if (imageUri == null) {
                Glide.with(bigImage.context).load(R.drawable.default_music_3).apply(options)
                    .into(bigImage)
            } else {
                Glide.with(bigImage.context).asBitmap().load(imageUri).apply(options)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            bigImage.setImageBitmap(resource)
                            Palette.from(resource).generate {
                                val origin = it?.dominantSwatch?.rgb ?: Color.TRANSPARENT
                                val mainColor = getReverseColor(origin)
                                MainPageViewModel.instance.mainPageTextColor.set(mainColor)
                                val secondColor = Color.argb((Color.alpha(mainColor) * 0.5).toInt(),
                                    Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor))
                                MainPageViewModel.instance.mainPageTextColorUncheck.set(secondColor)
                            }
                        }
                    })
            }
        }

        public fun getReverseColor(origin: Int): Int {
            val gray =
                (Color.red(origin) * 0.299 + Color.green(
                    origin
                ) * 0.587 + Color.blue(origin) * 0.114).toInt()

            return if (gray > 100) Color.BLACK else Color.WHITE
        }

        @JvmStatic
        @BindingAdapter("currentPercent")
        public fun setCurrentPercent(lyricView: MusicLyricView, currentPercent: Int) {
            lyricView.setPercent(currentPercent.toFloat().div(1000f))
        }

        @JvmStatic
        @BindingAdapter("currentTextColor")
        public fun setCurrentTextColor(lyricView: MusicLyricView, color: Int) {
            lyricView.frontColor = color
        }

        @JvmStatic
        @BindingAdapter("currentTextColorUncheck")
        public fun setCurrentTextColorUncheck(lyricView: MusicLyricView, color: Int) {
            lyricView.backColor = color
        }
    }


}