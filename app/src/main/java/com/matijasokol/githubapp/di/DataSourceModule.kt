package com.matijasokol.githubapp.di

import android.util.Log
import com.matijasokol.repo_datasource.RepoServiceImpl
import com.matijasokol.repo_domain.usecase.FetchReposUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Network", message)
                    }
                }
                level = LogLevel.ALL
            }
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