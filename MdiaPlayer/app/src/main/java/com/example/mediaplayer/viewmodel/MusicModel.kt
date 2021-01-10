package com.example.mediaplayer.viewmodel

import android.net.Uri
import androidx.databinding.ObservableField

class MusicModel {

    var imageUri =  ObservableField<Uri>()
    var musicName =  ObservableField<String>()
    var musicArtist =  ObservableField<String>()
    var musicPath = ObservableField<String>()

    constructor(imageUri: Uri, musicName: String, musicArtist: String, musicPath: String) {
        this.imageUri.set(imageUri)
        this.musicName.set(musicName)
        this.musicArtist.set(musicArtist)
        this.musicPath.set(musicPath)
    }
}