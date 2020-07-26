package com.example.mediaplayer.viewmodel

import androidx.databinding.ObservableField
import javax.inject.Inject

class UserViewModel @Inject constructor(){
    val userName = ObservableField<String>()
    val userId = ObservableField<String>()
}