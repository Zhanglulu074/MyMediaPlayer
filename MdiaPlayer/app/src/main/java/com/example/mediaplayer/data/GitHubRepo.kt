package com.example.mediaplayer.data

import com.google.gson.annotations.SerializedName

class GitHubRepo {
    var name: String = ""
    var id: Int = 0

    @SerializedName("private")
    var isPrivate: Boolean = false
}