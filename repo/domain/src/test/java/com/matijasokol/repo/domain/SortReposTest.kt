package com.matijasokol.repo.domain

import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo.datasourcetest.network.serializeRepoResponseData
import com.matijasokol.repo.domain.usecase.SortReposUseCase
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be greater or equal to`
import org.amshove.kluent.`should be less or equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SortReposTest {

    private lateinit var sut: SortReposUseCase

    private val repoList =
        serializeRepoResponseData(ClassLoader.getSystemResource("repo_list_valid.json").readText())

    @BeforeEach
    fun setUp() {
        sut = SortReposUseCase()
    }

    @Test
    fun `previous stars count SHOULD BE LESS OR EQUAL than current when ascending order is applied`() =
        runTest {
            val result = sut(
                repos = repoList,
                repoSortType = RepoSortType.Stars(SortOrder.Ascending),
            )

            for (index in 1 until result.size) {
                val previous = result[index - 1].starsCount
                val current = result[index].starsCount

                previous `should be less or equal to` current
            }
        }

    @Test
    fun `previous forks count SHOULD BE GREATER OR EQUAL than current when descending order is applied`() =
        runTest {
            val result = sut(
                repos = repoList,
                repoSortType = RepoSortType.Forks(SortOrder.Descending),
            )

            for (index in 1 until result.size) {
                val previous = result[index - 1].forksCount
                val current = result[index].forksCount

                previous `should be greater or equal to` current
            }
        }
}
