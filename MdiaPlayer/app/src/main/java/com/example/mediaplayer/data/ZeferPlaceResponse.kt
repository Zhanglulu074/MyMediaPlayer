package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

class ZeferPlaceResponse(@SerializedName("code") val status: String, val location: List<ZeferPlace>) {}

data class ZeferPlace(
    val name: String,
    val adm1: String,
    val adm2: String,
    val country: String,
    val lat: String,
    val lon: String
)

