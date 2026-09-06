package com.matijasokol.repo.detail

import arrow.core.left
import arrow.core.right
import com.matijasokol.core.error.NetworkError
import com.matijasokol.coreui.text.UiText
import com.matijasokol.repo.domain.DateUtils
import com.matijasokol.repo.domain.model.Author
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.persistentListOf
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.util.Date

class RepoDetailsUiMapperTest {

    private val sut = RepoDetailsUiMapper()

    @Test
    fun `should MAP static and formatted text`() {
        val state = sut.loadingState(repoFullName = "JetBrains/kotlin", authorImageUrl = "image")
        val profileAction = sut.toAction(RepoDetailEvent.OpenProfileWebError)
        val repoAction = sut.toAction(RepoDetailEvent.OpenRepoWebError)

        state.profileSupportingText shouldBeEqualTo
            UiText.StringResource(R.string.repo_detail_profile_label, "JetBrains")
        profileAction.message shouldBeEqualTo
            UiText.StringResource(R.string.repo_detail_message_profile_browser_error)
        repoAction.message shouldBeEqualTo
            UiText.StringResource(R.string.repo_detail_message_repo_browser_error)
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
            errorTitle = UiText.StringResource(R.string.repo_detail_error_title),
            loadErrorMessage = UiText.StringResource(R.string.repo_detail_message_cache_error),
            retryButtonText = UiText.StringResource(R.string.repo_detail_retry_text),
            repoFullName = "JetBrains/kotlin",
            authorImageUrl = "image",
            profileSupportingText = UiText.StringResource(R.string.repo_detail_profile_label, "JetBrains"),
        )
    }

    @Test
    fun `should RETURN SUCCESS STATE with repository text`() {
        val lastUpdated = Date(0)
        val repo = Repo(
            id = 1,
            name = "kotlin",
            fullName = "JetBrains/kotlin",
            author = Author(
                id = 2,
                name = "JetBrains",
                image = "author-image",
                profileUrl = "profile-url",
                followersUrl = "followers-url",
                reposUrl = "repos-url",
                followersCount = 10,
                reposCount = 20,
            ),
            watchersCount = 30,
            forksCount = 40,
            issuesCount = 50,
            lastUpdated = lastUpdated,
            starsCount = 60,
            topics = listOf("kotlin", "android"),
            language = "Kotlin",
            url = "repo-url",
            description = "Description",
        )

        val state = sut.toUiState(
            isLoading = false,
            repoOrError = repo.right(),
            repoFullName = repo.fullName,
            authorImageUrl = repo.author.image,
        )

        state shouldBeEqualTo RepoDetailState.Success(
            repositoryLinkTitle = UiText.StringResource(R.string.repo_detail_btn_repo_details),
            repositoryLinkSubtitle = UiText.StringResource(R.string.repo_detail_btn_repo_details_supporting),
            topicsSectionTitle = UiText.StringResource(R.string.repo_detail_topics_label),
            overviewSectionTitle = UiText.StringResource(R.string.repo_detail_overview_label),
            repoUi = RepoUi(
                repoUrl = "repo-url",
                info = persistentListOf(
                    UiText.StringResource(R.string.repo_detail_panel_watchers, 30),
                    UiText.StringResource(R.string.repo_detail_panel_issues, 50),
                    UiText.StringResource(R.string.repo_detail_panel_forks, 40),
                    UiText.StringResource(R.string.repo_detail_panel_stars, 60),
                    UiText.StringResource(R.string.repo_detail_panel_language, "Kotlin"),
                    UiText.StringResource(R.string.repo_detail_panel_description, "Description"),
                    UiText.StringResource(
                        R.string.repo_detail_panel_updated,
                        DateUtils.dateToLocalDateString(lastUpdated),
                    ),
                ),
                followersCountText = UiText.StringResource(R.string.repo_detail_followers_count_text, 10),
                reposCountText = UiText.StringResource(R.string.repo_detail_repos_count_text, 20),
                authorProfileUrl = "profile-url",
                topics = persistentListOf("kotlin", "android"),
            ),
            repoFullName = "JetBrains/kotlin",
            authorImageUrl = "author-image",
            profileSupportingText = UiText.StringResource(R.string.repo_detail_profile_label, "JetBrains"),
        )
    }
}
