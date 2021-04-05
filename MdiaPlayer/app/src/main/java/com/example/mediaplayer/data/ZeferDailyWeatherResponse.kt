package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

class ZeferDailyWeatherResponse(
    @SerializedName("code") val status: String, val daily: List<Daily>
)

class Daily(
    val fxDate: String,
    val tempMax: String,
    val tempMin: String,
    val textDay: String
)