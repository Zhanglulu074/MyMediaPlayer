package com.example.mediaplayer.cache

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class BitmapCache : ImageLoader.ImageCache {
    //LruCache对象

    //设置最大缓存为10Mb，大于这个值会启动自动回收
    private val max = 10 * 1024 * 1024


    private val lruCache = object : LruCache<String, Bitmap>(max) {
        override fun sizeOf(key: String?, value: Bitmap): Int {
            return value.rowBytes * value.height
        }
    }


    override fun getBitmap(url: String): Bitmap? {
        return lruCache[url]
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        lruCache.put(url, bitmap)
    }

    init {
        //初始化 LruCache

    }
}