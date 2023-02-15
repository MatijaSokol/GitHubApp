package com.matijasokol.repo_datasource_test.network

import com.matijasokol.repo_datasource.RepoServiceImpl
import com.matijasokol.repo_datasource.constants.NetworkConstants
import com.matijasokol.repo_domain.RepoService
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Url
import io.ktor.http.hostWithPort
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RepoServiceFake {

    companion object {
        private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
        private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

        private const val BASE_URL_TEST = NetworkConstants.BASE_URL + "?q=kotlin&per_page=30&page=0"

        fun build(
            type: RepoServiceResponseType
        ): RepoService {
            val fakeClient = HttpClient(MockEngine) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                engine {
                    addHandler { request ->
                        when (request.url.toString()) {
                            BASE_URL_TEST -> {
                                val responseHeaders = headersOf(
                                    "Content-Type" to listOf("application/json", "charset=utf-8")
                                )
                                when (type) {
                                    is RepoServiceResponseType.EmptyList -> {
                                        respond(
                                            ClassLoader.getSystemResource("repo_list_empty.json").readText(),
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is RepoServiceResponseType.MalformedData -> {
                                        respond(
                                            ClassLoader.getSystemResource("repo_list_malformed.json").readText(),
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is RepoServiceResponseType.GoodData -> {
                                        respond(
                                            ClassLoader.getSystemResource("repo_list_valid.json").readText(),
                                            status = HttpStatusCode.OK,
                                            headers = responseHeaders
                                        )
                                    }
                                    is RepoServiceResponseType.Http404 -> {
                                        respond(
                                            ClassLoader.getSystemResource("repo_list_invalid.json").readText(),
                                            status = HttpStatusCode.NotFound,
                                            headers = responseHeaders
                                        )
                                    }
                                }
                            }
                            else -> error("Unhandled ${request.url.fullUrl}")
                        }
                    }
                }
            }

            return RepoServiceImpl(httpClient = fakeClient)
        }
    }
}