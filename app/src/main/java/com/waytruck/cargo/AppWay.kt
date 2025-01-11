package com.waytruck.cargo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppWay :Application(){
    override fun onCreate() {
        super.onCreate()
    }
}