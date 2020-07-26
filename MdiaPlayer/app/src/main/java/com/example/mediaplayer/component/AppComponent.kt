package com.example.mediaplayer.component

import com.example.mediaplayer.ui.SecondActivity
import dagger.Component

@Component
interface AppComponent {
    fun inject(activity: SecondActivity)
}