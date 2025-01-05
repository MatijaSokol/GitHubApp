package com.matijasokol.repo.domain

import arrow.core.left
import arrow.core.right
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Date

@ExtendWith(MockKExtension::class)
class GetRepoFromDetailsTest {

    @RelaxedMockK
    private lateinit var repoService: RepoService

    @InjectMockKs
    private lateinit var sut: GetRepoDetailsUseCase

    private val repoName = "JetBrains/kotlin"

    @Test
    fun `should RETURN SUCCESS when request was successful`() = runTest {
        val expectedResult = buildFakeRepo()
        coEvery { repoService.fetchRepoDetails(repoName) } returns expectedResult.right()

        val actualResult = sut(repoName)

        actualResult.getOrNull() `should be` expectedResult
    }

    @Test
    fun `should RETURN ERROR when request fails`() = runTest {
        val expectedResult = NetworkError.UnknownNetworkError
        coEvery { repoService.fetchRepoDetails(repoName) } returns expectedResult.left()

        val actualResult = sut(repoName)

        actualResult.leftOrNull() `should be` expectedResult
    }
}

private fun buildFakeRepo() = Repo(
    id = 3432266,
    name = "kotlin",
    fullName = "JetBrains/kotlin",
    description = "The Kotlin Programming Language.",
    starsCount = 3432266,
    forksCount = 3432266,
    issuesCount = 3432266,
    lastUpdated = Date(),
    topics = emptyList(),
    language = "Kotlin",
    url = "",
    author = Author(
        id = 1,
        name = "JetBrains",
        image = "",
        profileUrl = "",
        followersUrl = "",
        reposUrl = "",
        followersCount = null,
        reposCount = null,
    ),
    watchersCount = 3432266,
)
