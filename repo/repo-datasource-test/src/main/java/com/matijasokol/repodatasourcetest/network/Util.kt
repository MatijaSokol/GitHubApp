package com.matijasokol.repodatasourcetest.network

import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repodatasource.mappers.toAuthor
import com.matijasokol.repodatasource.mappers.toRepo
import com.matijasokol.repodatasource.network.model.AuthorDto
import com.matijasokol.repodatasource.network.model.FetchReposResponse
import com.matijasokol.repodatasource.network.model.RepoDto
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
