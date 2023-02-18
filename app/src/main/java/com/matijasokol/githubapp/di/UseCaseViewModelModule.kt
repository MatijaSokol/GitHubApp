package com.matijasokol.githubapp.di

import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideFetchReposUseCase(paginator: Paginator): FetchReposUseCase {
        return FetchReposUseCase(paginator)
    }
}