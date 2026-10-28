package com.matijasokol.repo.datasource.di

import com.matijasokol.core.buildconfig.AppBuildConfig
import com.matijasokol.core.dispatchers.AppDispatchers
import com.matijasokol.core.logging.AppLogger
import com.matijasokol.repo.datasource.network.NetworkLogger
import com.matijasokol.repo.datasource.network.buildHttpClient
import com.matijasokol.repo.datasource.network.json
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = json

    @Provides
    @Singleton
    fun provideEngine(): HttpClientEngineFactory<*> = OkHttp

    @Provides
    @Singleton
    fun provideHttpClient(
        json: Json,
        engine: HttpClientEngineFactory<*>,
        appBuildConfig: AppBuildConfig,
        appLogger: AppLogger,
        appDispatchers: AppDispatchers,
    ): HttpClient = buildHttpClient(
        json = json,
        engine = engine,
        networkLogger = if (appBuildConfig.isDebug) NetworkLogger(appLogger) else null,
        config = { engine { dispatcher = appDispatchers.io } },
    )
}
