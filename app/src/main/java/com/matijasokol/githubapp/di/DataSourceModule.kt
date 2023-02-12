package com.matijasokol.githubapp.di

import com.matijasokol.repo_datasource.RepoServiceImpl
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideRepoService(httpClient: HttpClient): RepoServiceImpl {
        return RepoServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideFetchReposUseCase(repoService: RepoServiceImpl): FetchReposUseCase {
        return FetchReposUseCase(repoService)
    }
}