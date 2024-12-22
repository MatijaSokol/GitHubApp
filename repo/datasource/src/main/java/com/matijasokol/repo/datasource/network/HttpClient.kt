package com.matijasokol.repo.datasource.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun buildHttpClient(json: Json): HttpClient = HttpClient(OkHttp) {
    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        host = BASE_URL
        url { protocol = URLProtocol.HTTPS }
    }

    install(HttpTimeout) { requestTimeoutMillis = TIMEOUT_MS }

    install(ContentNegotiation) {
        json(json)
    }
}

const val BASE_URL = "api.github.com"
private const val TIMEOUT_MS = 60_000L
