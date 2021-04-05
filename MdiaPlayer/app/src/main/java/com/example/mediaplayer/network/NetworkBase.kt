package com.example.mediaplayer.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkBase {

//    const val WEATHER_KEY: String = "7e73f0d13ad34f4ba4b2f7d232e04a6f"
    const val WEATHER_KEY: String = "7e73f0d13ad34f4ba4b2f7d232e04a6f"

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://fy.iciba.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val placeService = Retrofit.Builder()
        .baseUrl("https://geoapi.qweather.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val weatherService = Retrofit.Builder()
        .baseUrl("https://devapi.qweather.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()



    fun<T> createRequest(requestClass: Class<T>): T = retrofit.create(requestClass)

    fun<T> createPlaceRequest(requestClass: Class<T>): T = placeService.create(requestClass)

    fun<T> createWeatherRequest(requestClass: Class<T>): T = weatherService.create(requestClass)


}