package com.matijasokol.repo.datasource.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun <T : HttpClientEngineConfig>buildHttpClient(
    json: Json,
    engine: HttpClientEngineFactory<T>,
    config: (HttpClientConfig<T>.() -> Unit)? = null,
): HttpClient = HttpClient(engine) {
    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        host = BASE_URL
        url { protocol = URLProtocol.HTTPS }
    }

    install(HttpTimeout) { requestTimeoutMillis = TIMEOUT_MS }

    install(ContentNegotiation) {
        json(json)
    }

    config?.invoke(this)
}

const val BASE_URL = "api.github.com"
private const val TIMEOUT_MS = 60_000L
