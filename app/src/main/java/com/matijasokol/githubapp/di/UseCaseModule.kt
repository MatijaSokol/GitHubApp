package com.matijasokol.githubapp.di

import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import com.matijasokol.repo_domain.usecase.SortReposUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFetchReposUseCase(paginator: Paginator): FetchReposUseCase {
        return FetchReposUseCase(paginator)
    }

    @Provides
    @Singleton
    fun provideSortReposUseCase(): SortReposUseCase {
        return SortReposUseCase()
    }
}