package com.matijasokol.githubapp.di

import android.app.Application
import coil3.ImageLoader
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        app: Application
    ): ImageLoader {
        return ImageLoader.Builder(app)
            .memoryCache(MemoryCache.Builder().maxSizePercent(app, 0.25).build())
            .crossfade(true)
            .build()
    }
}