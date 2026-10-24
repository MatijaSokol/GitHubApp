package com.matijasokol.repo.detail

import app.cash.turbine.test
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.coreui.text.UiText
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import com.matijasokol.test.coroutines.MainDispatcherExtension
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class RepoDetailViewModelTest {

    private lateinit var sut: RepoDetailViewModel

    private val destination = Destination.RepoDetail(repoFullName = "JetBrains/kotlin", authorImageUrl = "")
    private val repoDetailsUiMapper = RepoDetailsUiMapper()

    @Test
    fun `should RETURN SUCCESS STATE when request was successful`() = runTest {
        sut = buildSut()

        sut.state.test {
            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Success::class
        }
    }

    @Test
    fun `should RETURN ERROR STATE when request fails`() = runTest {
        sut = buildSut(responseType = RepoServiceResponseType.Http404)

        sut.state.test {
            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Error::class

            sut.onEvent(RepoDetailEvent.OnRetryClick)

            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Error::class
        }
    }

    @Test
    fun `should SEND PROFILE BROWSER ERROR ACTION when opening profile fails`() = runTest {
        sut = buildSut()

        sut.actions.test {
            sut.onEvent(RepoDetailEvent.OpenProfileWebError)

            awaitItem() shouldBeEqualTo RepoDetailAction.ShowMessage(
                message = UiText.StringResource(R.string.repo_detail_message_profile_browser_error),
            )
        }
    }

    @Test
    fun `should SEND REPO BROWSER ERROR ACTION when opening repository fails`() = runTest {
        sut = buildSut()

        sut.actions.test {
            sut.onEvent(RepoDetailEvent.OpenRepoWebError)

            awaitItem() shouldBeEqualTo RepoDetailAction.ShowMessage(
                message = UiText.StringResource(R.string.repo_detail_message_repo_browser_error),
            )
        }
    }

    private fun buildSut(
        responseType: RepoServiceResponseType = RepoServiceResponseType.GoodData,
    ) = RepoDetailViewModel(
        destination = destination,
        getRepoDetailsUseCase = GetRepoDetailsUseCase(
            repoService = RepoServiceFake.build(responseType),
        ),
        repoDetailsUiMapper = repoDetailsUiMapper,
    )
}
