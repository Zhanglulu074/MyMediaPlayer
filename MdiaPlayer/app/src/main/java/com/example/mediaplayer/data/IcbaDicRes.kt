package com.example.mediaplayer.data

import com.google.gson.annotations.SerializedName

class IcbaDicRes(val status: Int, val content: IcbaDicResContent)

class IcbaDicResContent(
    val from: String,
    val to: String,
    val vendor: String,
    val out: String,
    @SerializedName("err_no")
    var errorCode: String
)