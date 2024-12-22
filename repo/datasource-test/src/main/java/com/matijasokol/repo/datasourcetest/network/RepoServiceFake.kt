package com.matijasokol.repo.datasourcetest.network

import com.matijasokol.repo.datasource.network.BASE_URL
import com.matijasokol.repo.datasource.network.RepoServiceImpl
import com.matijasokol.repo.domain.RepoService
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.http.hostWithPort
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RepoServiceFake {

    private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
    private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

    private const val BASE_URL_TEST = "$BASE_URL?q=kotlin&per_page=30&page=0"
    private const val JETBRAINS_FOLLOWERS_URL = "https://api.github.com/users/JetBrains/followers"
    private const val JETBRAINS_REPOS_URL = "https://api.github.com/users/JetBrains/repos"

    fun build(
        type: RepoServiceResponseType,
    ): RepoService {
        val fakeClient = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
            engine {
                addHandler { request ->
                    when (request.url.toString()) {
                        BASE_URL_TEST -> handleRepoListRequest(type)
                        JETBRAINS_FOLLOWERS_URL -> handleJetBrainsFollowersRequest(type)
                        JETBRAINS_REPOS_URL -> handleJetBrainsReposRequest(type)
                        else -> error("Unhandled ${request.url.fullUrl}")
                    }
                }
            }
        }

        return RepoServiceImpl(httpClient = fakeClient)
    }
}

private fun MockRequestHandleScope.handleRepoListRequest(
    type: RepoServiceResponseType,
): HttpResponseData {
    val responseHeaders = headersOf(
        "Content-Type" to listOf("application/json", "charset=utf-8"),
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

private fun MockRequestHandleScope.handleJetBrainsFollowersRequest(
    type: RepoServiceResponseType,
): HttpResponseData {
    val responseHeaders = headersOf(
        "Content-Type" to listOf("application/json", "charset=utf-8"),
    )
    return when (type) {
        is RepoServiceResponseType.GoodData -> {
            respond(
                this::class.java.getResource("/jetbrains_followers.json").readText(),
                status = HttpStatusCode.OK,
                headers = responseHeaders,
            )
        }
        else -> throw NotImplementedError("Only GoodData case is implemented.")
    }
}

private fun MockRequestHandleScope.handleJetBrainsReposRequest(
    type: RepoServiceResponseType,
): HttpResponseData {
    val responseHeaders = headersOf(
        "Content-Type" to listOf("application/json", "charset=utf-8"),
    )
    return when (type) {
        is RepoServiceResponseType.GoodData -> {
            respond(
                this::class.java.getResource("/jetbrains_repos.json").readText(),
                status = HttpStatusCode.OK,
                headers = responseHeaders,
            )
        }
        else -> throw NotImplementedError("Only GoodData case is implemented.")
    }
}
