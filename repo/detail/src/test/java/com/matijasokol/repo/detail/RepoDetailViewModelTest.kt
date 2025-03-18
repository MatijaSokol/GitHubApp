package com.matijasokol.repo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.matijasokol.coreui.navigation.Destination
import com.matijasokol.repo.datasourcetest.network.RepoServiceFake
import com.matijasokol.repo.datasourcetest.network.RepoServiceResponseType
import com.matijasokol.repo.domain.usecase.GetRepoDetailsUseCase
import com.matijasokol.test.FakeDictionary
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be instance of`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(AndroidCoroutinesExtension::class)
class RepoDetailViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>()
    private val uiMapper = RepoDetailsUiMapper(FakeDictionary())

    @BeforeEach
    fun before() {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every {
            savedStateHandle.toRoute<Destination.RepoDetail>()
        } returns Destination.RepoDetail(repoFullName = "JetBrains/kotlin", authorImageUrl = "")
    }

    @Test
    fun `should RETURN SUCCESS STATE when request was successful`() = runTest {
        val getRepoDetailsUseCase = GetRepoDetailsUseCase(
            repoService = RepoServiceFake.build(
                RepoServiceResponseType.GoodData,
            ),
        )

        val sut = RepoDetailViewModel(
            savedStateHandle = savedStateHandle,
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
            savedStateHandle = savedStateHandle,
            getRepoDetails = getRepoDetailsUseCase,
            uiMapper = uiMapper,
            dictionary = FakeDictionary(),
        )

        sut.state.test {
            awaitItem() `should be instance of` RepoDetailState.Loading::class
            awaitItem() `should be instance of` RepoDetailState.Error::class
        }
    }

    @AfterEach
    fun after() {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }
}
