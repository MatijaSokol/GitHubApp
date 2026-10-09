package com.matijasokol.githubapp.di

import com.matijasokol.core.logging.AppLogger
import com.matijasokol.githubapp.logging.TimberLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggingModule {

    @Binds
    @Singleton
    abstract fun bindAppLogger(timberLogger: TimberLogger): AppLogger
}
