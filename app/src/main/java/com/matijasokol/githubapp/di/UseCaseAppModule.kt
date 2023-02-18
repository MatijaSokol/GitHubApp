package com.matijasokol.githubapp.di

import com.matijasokol.repo_domain.RepoCache
import com.matijasokol.repo_domain.RepoService
import com.matijasokol.repo_domain.usecase.GetRepoDetails
import com.matijasokol.repo_domain.usecase.SortReposUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseAppModule {

    @Provides
    @Singleton
    fun provideSortReposUseCase(): SortReposUseCase {
        return SortReposUseCase()
    }

    @Provides
    @Singleton
    fun provideGetRepoDetailsUseCase(
        repoService: RepoService,
        repoCache: RepoCache
    ): GetRepoDetails {
        return GetRepoDetails(
            repoService = repoService,
            repoCache = repoCache
        )
    }
}