package com.example.mediaplayer.network.services

import com.example.mediaplayer.data.IcbaDicRes
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestDicService {
    @GET("ajax.php?a=fy&f=auto&t=auto")
    fun getDicResQueryFor(@Query("w") word: String): Observable<IcbaDicRes>
}