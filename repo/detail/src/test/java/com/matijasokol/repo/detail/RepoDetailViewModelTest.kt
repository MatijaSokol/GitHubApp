package com.matijasokol.repo.detail

import app.cash.turbine.test
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import com.matijasokol.test.coroutines.MainDispatcherExtension
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be instance of`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class RepoDetailViewModelTest {

    private lateinit var sut: RepoDetailViewModel

    private val destination = Destination.RepoDetail(repoFullName = "JetBrains/kotlin", authorImageUrl = "")
    private val repoDetailsUiMapper = RepoDetailsUiMapper()

    @Test
    fun `should RETURN SUCCESS STATE when request was successful`() = runTest {
        val getRepoDetailsUseCase = GetRepoDetailsUseCase(
            repoService = RepoServiceFake.build(
                RepoServiceResponseType.GoodData,
            ),
        )

        sut = RepoDetailViewModel(
            destination = destination,
            getRepoDetailsUseCase = getRepoDetailsUseCase,
            repoDetailsUiMapper = repoDetailsUiMapper,
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

        sut = RepoDetailViewModel(
            destination = destination,
            getRepoDetailsUseCase = getRepoDetailsUseCase,
            repoDetailsUiMapper = repoDetailsUiMapper,
        )

        sut.state.test {
            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Error::class

            sut.onEvent(RepoDetailEvent.OnRetryClick)

            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Error::class
        }
    }
}
