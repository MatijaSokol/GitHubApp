package com.matijasokol.githubapp.di

import android.app.Application
import com.matijasokol.repo_datasource.cache.RepoDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
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
            schema = RepoDatabase.Schema,
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