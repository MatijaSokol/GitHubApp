package com.matijasokol.githubapp

import android.app.Application
import com.matijasokol.githubapp.logging.initLogging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogging(isDebug = BuildConfig.DEBUG)
    }
}
