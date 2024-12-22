package com.matijasokol.repo.datasource.network

import arrow.core.Either
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.datasource.mappers.toRepo
import com.matijasokol.repo.datasource.network.model.FetchReposResponse
import com.matijasokol.repo.datasource.network.model.RepoDto
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repo.domain.model.Repo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.path
import javax.inject.Inject

class RepoServiceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : RepoService {

    override suspend fun fetchRepos(
        query: String,
        perPage: Int,
        page: Int,
    ): Either<NetworkError, List<Repo>> = safeNetworkCall {
        httpClient.get {
            url {
                path(SEARCH_ENDPOINT)
                parameter(PARAM_QUERY, query)
                parameter(PARAM_PER_PAGE, perPage)
                parameter(PARAM_PAGE, page)
            }
        }.body<FetchReposResponse>().repos.map(RepoDto::toRepo)
    }

    override suspend fun fetchRepoDetails(repoName: String): Either<NetworkError, Repo> = safeNetworkCall {
        httpClient.get {
            url {
                path(DETAILS_ENDPOINT)
                // repositoryName can have special characters so encoding is enabled
                appendEncodedPathSegments(repoName)
            }
        }.body<RepoDto>().toRepo()
    }
}

private const val PARAM_QUERY = "q"
private const val PARAM_PER_PAGE = "per_page"
private const val PARAM_PAGE = "page"

private const val SEARCH_ENDPOINT = "search/repositories"
private const val DETAILS_ENDPOINT = "repos"
