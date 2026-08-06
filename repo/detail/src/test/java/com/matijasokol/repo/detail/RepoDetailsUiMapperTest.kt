package com.matijasokol.repo.detail

import arrow.core.left
import com.matijasokol.core.error.NetworkError
import com.matijasokol.test.FakeDictionary
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class RepoDetailsUiMapperTest {

    private val sut = RepoDetailsUiMapper(
        FakeDictionary(
            getString = mapOf(
                R.string.repo_detail_error_title to "Repository unavailable",
                R.string.repo_detail_message_cache_error to "Could not load details",
                R.string.repo_detail_retry_text to "Try again",
                R.string.repo_detail_btn_repo_details to "View on GitHub",
                R.string.repo_detail_btn_repo_details_supporting to "Open in browser",
                R.string.repo_detail_topics_label to "Topics",
                R.string.repo_detail_overview_label to "At a glance",
                R.string.repo_detail_message_profile_browser_error to "Could not open profile",
                R.string.repo_detail_message_repo_browser_error to "Could not open repository",
            )::getValue,
            getStringArgs = { _, args -> "Maintained by ${args.single()}" },
        ),
    )

    @Test
    fun `should MAP static and formatted text`() {
        val state = sut.loadingState(repoFullName = "JetBrains/kotlin", authorImageUrl = "image")
        val action = sut.toAction(RepoDetailEvent.OpenProfileWebError)

        state.profileSupportingText shouldBeEqualTo "Maintained by JetBrains"
        action.message shouldBeEqualTo "Could not open profile"
    }

    @Test
    fun `should RETURN ERROR STATE when repository details fail to load`() {
        val state = sut.toUiState(
            isLoading = false,
            repoOrError = NetworkError.UnknownNetworkError.left(),
            repoFullName = "JetBrains/kotlin",
            authorImageUrl = "image",
        ) as RepoDetailState.Error

        state shouldBeEqualTo RepoDetailState.Error(
            errorTitle = "Repository unavailable",
            loadErrorMessage = "Could not load details",
            retryButtonText = "Try again",
            repoFullName = "JetBrains/kotlin",
            authorImageUrl = "image",
            profileSupportingText = "Maintained by JetBrains",
        )
    }
}
