package com.matijasokol.repo.detail

import app.cash.turbine.test
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import com.matijasokol.test.FakeDictionary
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be instance of`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AndroidCoroutinesExtension::class)
class RepoDetailViewModelTest {

    private val destination = Destination.RepoDetail(repoFullName = "JetBrains/kotlin", authorImageUrl = "")
    private val uiMapper = RepoDetailsUiMapper(FakeDictionary())

    @Test
    fun `should RETURN SUCCESS STATE when request was successful`() = runTest {
        val getRepoDetailsUseCase = GetRepoDetailsUseCase(
            repoService = RepoServiceFake.build(
                RepoServiceResponseType.GoodData,
            ),
        )

        val sut = RepoDetailViewModel(
            destination = destination,
            getRepoDetails = getRepoDetailsUseCase,
            uiMapper = uiMapper,
            dictionary = FakeDictionary(),
        )

        sut.state.test {
            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Success::class
        }
    }

    @Test
    fun `should RETURN ERROR STATE when request fails`() = runTest {
        val getRepoDetailsUseCase = GetRepoDetailsUseCase(
            repoService = RepoServiceFake.build(
                RepoServiceResponseType.Http404,
            ),
        )

        val sut = RepoDetailViewModel(
            destination = destination,
            getRepoDetails = getRepoDetailsUseCase,
            uiMapper = uiMapper,
            dictionary = FakeDictionary(),
        )

        sut.state.test {
            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Error::class
        }
    }
}
