package com.matijasokol.repo_datasource_test.network

import com.matijasokol.repo_datasource.mappers.toAuthor
import com.matijasokol.repo_datasource.mappers.toRepo
import com.matijasokol.repo_datasource.network.model.AuthorDto
import com.matijasokol.repo_datasource.network.model.FetchReposResponse
import com.matijasokol.repo_datasource.network.model.RepoDto
import com.matijasokol.repo_domain.model.Author
import com.matijasokol.repo_domain.model.Repo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
}

fun serializeRepoResponseData(jsonData: String): List<Repo> {
    return json.decodeFromString<FetchReposResponse>(jsonData).repos.map { it.toRepo() }
}

fun serializeRepoListData(jsonData: String): List<Repo> {
    return json.decodeFromString<List<RepoDto>>(jsonData).map { it.toRepo() }
}

fun serializeAuthorListData(jsonData: String): List<Author> {
    return json.decodeFromString<List<AuthorDto>>(jsonData).map { it.toAuthor() }
}