package com.matijasokol.githubapp.di

import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repodatasource.cache.RepoCacheImpl
import com.matijasokol.repodatasource.network.RepoServiceImpl
import dagger.Binds
import dagger.Module
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
}
