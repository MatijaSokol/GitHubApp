package com.matijasokol.repo.domain

import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.domain.usecase.SortReposUseCase
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SortReposTest {

    private lateinit var sortRepos: SortReposUseCase

    private val repoList = serializeRepoResponseData(ClassLoader.getSystemResource("repo_list_valid.json").readText())

    @BeforeEach
    fun setUp() {
        sortRepos = SortReposUseCase()
    }

    @Test
    fun orderByStarsAsc() = runBlocking {
        val result = sortRepos.execute(
            repos = repoList,
            repoSortType = RepoSortType.Stars(SortOrder.Ascending),
        )

        for (index in 1 until result.size) {
            assert(result[index - 1].starsCount <= result[index].starsCount)
        }
    }

    @Test
    fun orderByForksDesc() = runBlocking {
        val result = sortRepos.execute(
            repos = repoList,
            repoSortType = RepoSortType.Forks(SortOrder.Descending),
        )

        for (index in 1 until result.size) {
            assert(result[index - 1].forksCount >= result[index].forksCount)
        }
    }
}
