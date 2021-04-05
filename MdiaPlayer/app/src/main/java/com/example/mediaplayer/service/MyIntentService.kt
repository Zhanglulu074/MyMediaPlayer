package com.example.mediaplayer.service

import android.app.IntentService
import android.content.Intent
import android.os.HandlerThread
import android.util.Log

class MyIntentService : IntentService {
    constructor(name: String?) : super(name)

    override fun onHandleIntent(intent: Intent?) {
        Log.d("zll", "Hello World: ")
    }
}