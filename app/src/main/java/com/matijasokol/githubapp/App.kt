package com.matijasokol.githubapp

import android.app.Application
import com.matijasokol.core.buildconfig.AppBuildConfig
import com.matijasokol.githubapp.logging.initLogging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var appBuildConfig: AppBuildConfig

    override fun onCreate() {
        super.onCreate()
        initLogging(isDebug = appBuildConfig.isDebug)
    }
}
