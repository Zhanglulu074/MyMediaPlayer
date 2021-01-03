package com.example.mediaplayer.util

import androidx.databinding.BindingAdapter
import com.example.mediaplayer.widget.MusicProgressButton
import java.lang.StringBuilder
import java.text.DecimalFormat

class Util {
    companion object {
        public fun getTimeString(seconds: Int) : String {
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
    }


}