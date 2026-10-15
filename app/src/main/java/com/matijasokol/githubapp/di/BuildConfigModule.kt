package com.matijasokol.githubapp.di

import com.matijasokol.core.buildconfig.AppBuildConfig
import com.matijasokol.githubapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildConfigModule {

    @Provides
    @Singleton
    fun provideAppBuildConfig(): AppBuildConfig = AppBuildConfig(
        isDebug = BuildConfig.DEBUG,
        applicationId = BuildConfig.APPLICATION_ID,
        buildType = BuildConfig.BUILD_TYPE,
        flavor = BuildConfig.FLAVOR,
        versionName = BuildConfig.VERSION_NAME,
        versionCode = BuildConfig.VERSION_CODE,
    )
}
