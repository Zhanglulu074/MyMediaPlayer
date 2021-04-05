package com.example.mediaplayer.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mediaplayer.R
import kotlinx.android.synthetic.main.activity_stack_other.*

class StackOtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_other)
        Log.i("WooYun", "onCreate：" + javaClass.simpleName + " TaskId: " + taskId + " hasCode:" + this.hashCode());
        btn_in_other.setOnClickListener {
            startActivity(Intent(this, StackTargetActivity::class.java))
        }
        dumpTaskAffinity()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("WooYun", "onNewIntent：" + javaClass.simpleName + " TaskId: " + taskId + " hasCode:" + this.hashCode());
    }

    private fun dumpTaskAffinity() {
        try {
            val info = this.packageManager
                .getActivityInfo(componentName, PackageManager.GET_META_DATA)
            Log.i("WooYun", "taskAffinity:" + info.taskAffinity)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}