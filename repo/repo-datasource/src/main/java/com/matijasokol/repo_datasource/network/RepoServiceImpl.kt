package com.matijasokol.repo_datasource.network

import com.matijasokol.repo_datasource.mappers.toAuthor
import com.matijasokol.repo_datasource.mappers.toRepo
import com.matijasokol.repo_datasource.network.model.AuthorDto
import com.matijasokol.repo_datasource.network.model.FetchReposResponse
import com.matijasokol.repo_datasource.network.model.RepoDto
import com.matijasokol.repo_domain.model.Repo
import com.matijasokol.repo_domain.RepoService
import com.matijasokol.repo_domain.model.Author
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

    override suspend fun fetchAuthorFollowers(followersUrl: String): List<Author> {
        val result = httpClient.get {
            url(followersUrl)
            contentType(ContentType.Application.Json)
        }

        return result.body<List<AuthorDto>>().map { it.toAuthor() }
    }

    override suspend fun fetchAuthorRepos(reposUrl: String): List<Repo> {
        val result = httpClient.get {
            url(reposUrl)
            contentType(ContentType.Application.Json)
        }

        return result.body<List<RepoDto>>().map { it.toRepo() }
    }
}