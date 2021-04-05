package com.example.mediaplayer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.R
import com.example.mediaplayer.network.NetworkBase
import com.example.mediaplayer.network.services.RequestWeatherService
import com.sunnyweather.android.logic.model.ZeferDailyWeatherResponse
import com.sunnyweather.android.logic.model.ZeferPlaceResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_stack_target.*
import retrofit2.Call
import java.util.concurrent.TimeUnit
import retrofit2.Callback
import retrofit2.Response


class StackTargetActivity : AppCompatActivity() {

    private val co: CompositeDisposable = CompositeDisposable()

    companion object {
        const val TAG: String = "StackTargetActivity"
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_target)
        Log.i(
            "WooYun",
            "onCreate：" + javaClass.simpleName + " TaskId: " + taskId + " hasCode:" + this.hashCode()
        );
        btn_to_other.setOnClickListener {
            startActivity(Intent(this, StackOtherActivity::class.java))
        }
//        val a = Observable.interval(1, 1, TimeUnit.SECONDS)
//            .doOnNext {
//                val dicObservable = NetworkBase.createRequest(RequestDicService::class.java)
//                    .getDicResQueryFor("HelloWord")
//                dicObservable.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ res: IcbaDicRes -> Log.d(TAG, "onCreate: $res") },
//                        { error: Throwable -> Log.d(TAG, "onCreate: request failed $error") })
//                    .addTo(co)
//            }.subscribe {
//                Log.d(TAG, "onCreate: ")
//            }.addTo(co)

        val netWorkObservable1 =
            NetworkBase.createPlaceRequest(RequestWeatherService::class.java).searchPlaces("石家庄")

        val netWorkObservable2 =
            NetworkBase.createPlaceRequest(RequestWeatherService::class.java).searchPlaces("太原")
//        netWorkObservable.subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .doOnNext {
//                Log.d(TAG, "onCreate: ${it.location.size}")
//            }
//            .flatMap {
//                val lng = it.location[0].lon
//                val lat = it.location[0].lat
//                val location = "$lng,$lat"
//                NetworkBase.createWeatherRequest(RequestWeatherService::class.java).getZeferDailyWeather(location)
//            }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                Log.d(TAG, "onCreate: $it")
//            }.addTo(co)

        Observable.zip(
            netWorkObservable1,
            netWorkObservable2,
            BiFunction<ZeferPlaceResponse, ZeferPlaceResponse, String> { p1, p2 ->
                "${p1.location[0].adm1}  ${p2.location[0].adm1}"
            }).subscribeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG, "onCreate: $it")
            }


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(
            "WooYun",
            "onNewIntent：" + javaClass.simpleName + " TaskId: " + taskId + " hasCode:" + this.hashCode()
        );
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

    override fun onDestroy() {
        super.onDestroy()
        co.clear()
    }

    fun Disposable.addTo(co: CompositeDisposable) {
        co.add(this)
    }
}