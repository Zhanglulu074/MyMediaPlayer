package com.example.mediaplayer.util

import com.example.mediaplayer.data.LyricBean
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.regex.Pattern

class LyricUtil {
    companion object {
        public fun parseLyricFile(file: File): MutableList<LyricBean> {
            var resList: MutableList<LyricBean> = mutableListOf()
            if (!file.exists()) {
                return resList
            }
            var fileInputStream: FileInputStream? = null
            val byteArray = ByteArray(file.length().toInt())
            try {
                fileInputStream = FileInputStream(file)
                fileInputStream.read(byteArray)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {

            }
            val resStr: String = Test.test(byteArray)
            val split: List<String> = resStr.split("\n")
            for (i in split.indices) {
                val lrc = split[i]
                var pattern = "\\[\\d+:\\d+\\.\\d+].*\r"
                if (Pattern.matches(pattern, lrc)) {
                    lrc.replace("\r\n", "")
                    val min: String = lrc.substring(lrc.indexOf("[") + 1, lrc.indexOf("[") + 3)
                    val seconds: String =
                        lrc.substring(lrc.indexOf(":") + 1, lrc.indexOf(":") + 3)
                    val mills: String = lrc.substring(lrc.indexOf(".") + 1, lrc.indexOf(".") + 3)
                    val startTime =
                        java.lang.Long.valueOf(min) * 60 * 1000 + java.lang.Long.valueOf(seconds) * 1000 + java.lang.Long.valueOf(
                            mills
                        ) * 10
                    val text: String = lrc.substring(lrc.indexOf("]") + 1)
                    val bean: LyricBean = LyricBean(startTime, 0L ,text)
                    resList.add(bean)
                    if (resList.size > 1) {
                        resList[resList.size - 2].endTime = startTime
                    }
                }
            }
            return resList
        }
    }
}