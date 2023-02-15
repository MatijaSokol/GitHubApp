package com.matijasokol.repo_datasource.network

import com.matijasokol.repo_datasource.mappers.toRepo
import com.matijasokol.repo_datasource.network.model.FetchReposResponse
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.repo_domain.RepoService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.request.parameter
import io.ktor.http.contentType
import io.ktor.http.ContentType
import javax.inject.Inject

class RepoServiceImpl @Inject constructor(
    private val httpClient: HttpClient
) : RepoService {

    override suspend fun fetchRepos(query: String, perPage: Int, page: Int): List<Repo> {
        val result = httpClient.get {
            url(NetworkConstants.BASE_URL)
            contentType(ContentType.Application.Json)
            parameter(NetworkConstants.PARAM_QUERY, query)
            parameter(NetworkConstants.PARAM_PER_PAGE, perPage)
            parameter(NetworkConstants.PARAM_PAGE, page)
        }

        return result.body<FetchReposResponse>().repos.map { it.toRepo() }
    }
}