package com.matijasokol.githubapp.di

import android.app.Application
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.matijasokol.repo_datasource.cache.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideAndroidDriver(application: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = RepoDatabase.Schema.synchronous(),
            context = application,
            name = "repos.db"
        )
    }

    @Provides
    @Singleton
    fun provideRepoDatabase(sqlDriver: SqlDriver): RepoDatabase {
        return RepoDatabase(sqlDriver)
    }
}