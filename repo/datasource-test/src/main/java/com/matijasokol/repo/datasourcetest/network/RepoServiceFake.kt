package com.matijasokol.repo.datasourcetest.network

import com.matijasokol.repo.datasource.network.BASE_URL
import com.matijasokol.repo.datasource.network.RepoServiceImpl
import com.matijasokol.repo.datasource.network.buildHttpClient
import com.matijasokol.repo.domain.RepoService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.hostWithPort
import io.ktor.http.withCharset
import kotlinx.serialization.json.Json

object RepoServiceFake {

    private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
    private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

    private const val BASE_LIST_URL_TEST = "https://$BASE_URL/search/repositories?q=kotlin&per_page=30&page="
    private const val BASE_DETAILS_URL_TEST = "https://$BASE_URL/repos/JetBrains/kotlin"

    fun build(
        type: RepoServiceResponseType,
    ): RepoService {
        val fakeClient = buildHttpClient(
            json = Json { ignoreUnknownKeys = true },
            engine = MockEngine,
            config = {
                engine {
                    addHandler { request ->
                        val url = request.url.toString()

                        when {
                            url.startsWith(BASE_LIST_URL_TEST) -> handleRepoListRequest(type)
                            url == BASE_DETAILS_URL_TEST -> handleRepoDetailsRequest(type)
                            else -> error("Unhandled ${request.url.fullUrl}")
                        }
                    }
                }
            },
        )

        return RepoServiceImpl(httpClient = fakeClient)
    }
}

private fun MockRequestHandleScope.handleRepoListRequest(
    type: RepoServiceResponseType,
): HttpResponseData {
    val responseHeaders = headersOf(
        name = HttpHeaders.ContentType,
        value = ContentType.Application.Json.withCharset(Charsets.UTF_8).toString(),
    )

    return when (type) {
        is RepoServiceResponseType.EmptyList -> {
            respond(
                this::class.java.getResource("/repo_list_empty.json").readText(),
                status = HttpStatusCode.OK,
                headers = responseHeaders,
            )
        }
        is RepoServiceResponseType.MalformedData -> {
            respond(
                this::class.java.getResource("/repo_list_malformed.json").readText(),
                status = HttpStatusCode.OK,
                headers = responseHeaders,
            )
        }
        is RepoServiceResponseType.GoodData -> {
            respond(
                this::class.java.getResource("/repo_list_valid.json").readText(),
                status = HttpStatusCode.OK,
                headers = responseHeaders,
            )
        }
        is RepoServiceResponseType.Http404 -> {
            respond(
                this::class.java.getResource("/repo_list_invalid.json").readText(),
                status = HttpStatusCode.NotFound,
                headers = responseHeaders,
            )
        }
    }
}

private fun MockRequestHandleScope.handleRepoDetailsRequest(
    type: RepoServiceResponseType,
): HttpResponseData {
    val responseHeaders = headersOf(
        name = HttpHeaders.ContentType,
        value = ContentType.Application.Json.withCharset(Charsets.UTF_8).toString(),
    )

    return when (type) {
        is RepoServiceResponseType.GoodData -> {
            respond(
                this::class.java.getResource("/jetbrains_kotlin_details.json").readText(),
                status = HttpStatusCode.OK,
                headers = responseHeaders,
            )
        }
        else -> throw NotImplementedError("Only GoodData case is implemented.")
    }
}
