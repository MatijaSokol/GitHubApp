package com.matijasokol.githubapp.di

import com.matijasokol.repo_datasource.BasicPaginator
import com.matijasokol.repo_domain.Paginator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {

    @Binds
    @Singleton
    abstract fun provideBasicPaginator(basicPaginator: BasicPaginator): Paginator
}