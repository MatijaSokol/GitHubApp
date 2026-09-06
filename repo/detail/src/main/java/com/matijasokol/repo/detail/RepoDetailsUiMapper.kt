package com.matijasokol.repo.detail

import arrow.core.Either
import com.matijasokol.core.error.NetworkError
import com.matijasokol.coreui.text.UiText
import com.matijasokol.repo.domain.DateUtils
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class RepoDetailsUiMapper @Inject constructor() {

    private val text = mapText()

    fun toUiState(
        isLoading: Boolean,
        repoOrError: Either<NetworkError, Repo>,
        repoFullName: String,
        authorImageUrl: String,
    ) = when (isLoading) {
        true -> loadingState(repoFullName, authorImageUrl)
        false -> when (repoOrError) {
            is Either.Left -> RepoDetailState.Error(
                errorTitle = text.errorTitle,
                loadErrorMessage = text.loadErrorMessage,
                retryButtonText = text.retryButtonText,
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
                profileSupportingText = profileSupportingText(repoFullName),
            )
            is Either.Right -> RepoDetailState.Success(
                repositoryLinkTitle = text.repositoryLinkTitle,
                repositoryLinkSubtitle = text.repositoryLinkSubtitle,
                topicsSectionTitle = text.topicsSectionTitle,
                overviewSectionTitle = text.overviewSectionTitle,
                repoUi = RepoUi(
                    repoUrl = repoOrError.value.url,
                    info = buildInfoData(repoOrError.value),
                    authorProfileUrl = repoOrError.value.author.profileUrl,
                    topics = repoOrError.value.topics.toPersistentList(),
                    followersCountText = repoOrError.value.author.followersCount?.let {
                        UiText.StringResource(R.string.repo_detail_followers_count_text, it)
                    },
                    reposCountText = repoOrError.value.author.reposCount?.let {
                        UiText.StringResource(R.string.repo_detail_repos_count_text, it)
                    },
                ),
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
                profileSupportingText = profileSupportingText(repoFullName),
            )
        }
    }

    private fun mapText(): RepoDetailText = RepoDetailText(
        errorTitle = UiText.StringResource(R.string.repo_detail_error_title),
        loadErrorMessage = UiText.StringResource(R.string.repo_detail_message_cache_error),
        retryButtonText = UiText.StringResource(R.string.repo_detail_retry_text),
        repositoryLinkTitle = UiText.StringResource(R.string.repo_detail_btn_repo_details),
        repositoryLinkSubtitle = UiText.StringResource(R.string.repo_detail_btn_repo_details_supporting),
        topicsSectionTitle = UiText.StringResource(R.string.repo_detail_topics_label),
        overviewSectionTitle = UiText.StringResource(R.string.repo_detail_overview_label),
    )

    fun loadingState(repoFullName: String, authorImageUrl: String) = RepoDetailState.Loading(
        repoFullName = repoFullName,
        authorImageUrl = authorImageUrl,
        profileSupportingText = profileSupportingText(repoFullName),
    )

    fun toAction(event: RepoDetailEvent) = RepoDetailAction.ShowMessage(
        message = when (event) {
            RepoDetailEvent.OpenProfileWebError ->
                UiText.StringResource(R.string.repo_detail_message_profile_browser_error)
            RepoDetailEvent.OpenRepoWebError ->
                UiText.StringResource(R.string.repo_detail_message_repo_browser_error)
            RepoDetailEvent.OnRetryClick -> error("Retry does not produce a UI action")
        },
    )

    private fun profileSupportingText(repoFullName: String) = UiText.StringResource(
        R.string.repo_detail_profile_label,
        repoFullName.substringBefore("/"),
    )

    private fun buildInfoData(repo: Repo): ImmutableList<UiText> = persistentListOf(
        UiText.StringResource(R.string.repo_detail_panel_watchers, repo.watchersCount),
        UiText.StringResource(R.string.repo_detail_panel_issues, repo.issuesCount),
        UiText.StringResource(R.string.repo_detail_panel_forks, repo.forksCount),
        UiText.StringResource(R.string.repo_detail_panel_stars, repo.starsCount),
        UiText.StringResource(R.string.repo_detail_panel_language, repo.language.orEmpty()),
        UiText.StringResource(R.string.repo_detail_panel_description, repo.description.orEmpty()),
        UiText.StringResource(
            R.string.repo_detail_panel_updated,
            DateUtils.dateToLocalDateString(repo.lastUpdated),
        ),
    )
}
