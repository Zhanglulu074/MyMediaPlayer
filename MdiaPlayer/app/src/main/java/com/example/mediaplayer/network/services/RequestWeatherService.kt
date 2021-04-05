package com.example.mediaplayer.network.services

import com.example.mediaplayer.network.NetworkBase
import com.sunnyweather.android.logic.model.ZeferDailyWeatherResponse
import com.sunnyweather.android.logic.model.ZeferPlaceResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestWeatherService {

    @GET("v2/city/lookup?key=${NetworkBase.WEATHER_KEY}")
    fun searchPlaces(@Query("location") query: String): Observable<ZeferPlaceResponse>

    @GET("v7/weather/3d?key=${NetworkBase.WEATHER_KEY}")
    fun getZeferDailyWeather(@Query("location") lat: String): Observable<ZeferDailyWeatherResponse>
}