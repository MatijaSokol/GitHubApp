package com.matijasokol.repo.datasource.di

import app.cash.sqldelight.db.SqlDriver
import com.matijasokol.repo.datasource.cache.RepoCacheImpl
import com.matijasokol.repo.datasource.cache.RepoDatabase
import com.matijasokol.repo.datasource.network.RepoServiceImpl
import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.RepoService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRepoService(repoServiceImpl: RepoServiceImpl): RepoService

    @Binds
    @Singleton
    abstract fun bindRepoCache(repoCacheImpl: RepoCacheImpl): RepoCache

    companion object {

        @Provides
        @Singleton
        fun provideRepoDatabase(sqlDriver: SqlDriver): RepoDatabase = RepoDatabase(sqlDriver)
    }
}
