package com.matijasokol.githubapp.di

import com.matijasokol.repo.datasource.network.buildHttpClient
import com.matijasokol.repo.datasource.network.json
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson() = json

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient = buildHttpClient(json)
}
