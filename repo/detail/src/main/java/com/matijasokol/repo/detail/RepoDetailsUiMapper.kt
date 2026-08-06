package com.matijasokol.repo.detail

import arrow.core.Either
import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.domain.DateUtils
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class RepoDetailsUiMapper @Inject constructor(private val dictionary: Dictionary) {

    private data class DetailsStaticData(
        val errorTitle: String,
        val loadErrorMessage: String,
        val retryButtonText: String,
        val repositoryLinkTitle: String,
        val repositoryLinkSubtitle: String,
        val topicsSectionTitle: String,
        val overviewSectionTitle: String,
        val profileBrowserErrorMessage: String,
        val repoBrowserErrorMessage: String,
    )

    private val staticData by lazy {
        DetailsStaticData(
            errorTitle = dictionary.getString(R.string.repo_detail_error_title),
            loadErrorMessage = dictionary.getString(R.string.repo_detail_message_cache_error),
            retryButtonText = dictionary.getString(R.string.repo_detail_retry_text),
            repositoryLinkTitle = dictionary.getString(R.string.repo_detail_btn_repo_details),
            repositoryLinkSubtitle = dictionary.getString(R.string.repo_detail_btn_repo_details_supporting),
            topicsSectionTitle = dictionary.getString(R.string.repo_detail_topics_label),
            overviewSectionTitle = dictionary.getString(R.string.repo_detail_overview_label),
            profileBrowserErrorMessage = dictionary.getString(R.string.repo_detail_message_profile_browser_error),
            repoBrowserErrorMessage = dictionary.getString(R.string.repo_detail_message_repo_browser_error),
        )
    }

    fun toUiState(
        isLoading: Boolean,
        repoOrError: Either<NetworkError, Repo>,
        repoFullName: String,
        authorImageUrl: String,
    ) = when (isLoading) {
        true -> loadingState(repoFullName, authorImageUrl)
        false -> when (repoOrError) {
            is Either.Left -> RepoDetailState.Error(
                errorTitle = staticData.errorTitle,
                loadErrorMessage = staticData.loadErrorMessage,
                retryButtonText = staticData.retryButtonText,
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
                profileSupportingText = profileSupportingText(repoFullName),
            )
            is Either.Right -> RepoDetailState.Success(
                repositoryLinkTitle = staticData.repositoryLinkTitle,
                repositoryLinkSubtitle = staticData.repositoryLinkSubtitle,
                topicsSectionTitle = staticData.topicsSectionTitle,
                overviewSectionTitle = staticData.overviewSectionTitle,
                repoUi = RepoUi(
                    repoUrl = repoOrError.value.url,
                    info = buildInfoData(repoOrError.value),
                    authorProfileUrl = repoOrError.value.author.profileUrl,
                    topics = repoOrError.value.topics.toPersistentList(),
                    followersCountText = repoOrError.value.author.followersCount?.let {
                        dictionary.getString(R.string.repo_detail_followers_count_text, it)
                    },
                    reposCountText = repoOrError.value.author.reposCount?.let {
                        dictionary.getString(R.string.repo_detail_repos_count_text, it)
                    },
                ),
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
                profileSupportingText = profileSupportingText(repoFullName),
            )
        }
    }

    fun loadingState(repoFullName: String, authorImageUrl: String) = RepoDetailState.Loading(
        repoFullName = repoFullName,
        authorImageUrl = authorImageUrl,
        profileSupportingText = profileSupportingText(repoFullName),
    )

    fun toAction(event: RepoDetailEvent) = RepoDetailAction.ShowMessage(
        message = when (event) {
            RepoDetailEvent.OpenProfileWebError -> staticData.profileBrowserErrorMessage
            RepoDetailEvent.OpenRepoWebError -> staticData.repoBrowserErrorMessage
            RepoDetailEvent.OnRetryClick -> error("Retry does not produce a UI action")
        },
    )

    private fun profileSupportingText(repoFullName: String) = dictionary.getString(
        R.string.repo_detail_profile_label,
        repoFullName.substringBefore("/"),
    )

    private fun buildInfoData(repo: Repo): ImmutableList<String> = with(dictionary) {
        persistentListOf(
            getString(R.string.repo_detail_panel_watchers, repo.watchersCount),
            getString(R.string.repo_detail_panel_issues, repo.issuesCount),
            getString(R.string.repo_detail_panel_forks, repo.forksCount),
            getString(R.string.repo_detail_panel_stars, repo.starsCount),
            getString(R.string.repo_detail_panel_language, repo.language.orEmpty()),
            getString(R.string.repo_detail_panel_description, repo.description.orEmpty()),
            getString(
                R.string.repo_detail_panel_updated,
                DateUtils.dateToLocalDateString(repo.lastUpdated),
            ),
        )
    }
}
