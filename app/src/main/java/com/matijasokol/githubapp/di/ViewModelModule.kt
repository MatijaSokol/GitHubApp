package com.matijasokol.githubapp.di

import com.matijasokol.repo_datasource.BasicPaginator
import com.matijasokol.repo_domain.Paginator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun provideBasicPaginator(basicPaginator: BasicPaginator): Paginator
}