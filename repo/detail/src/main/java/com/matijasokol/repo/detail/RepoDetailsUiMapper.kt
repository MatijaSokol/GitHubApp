package com.matijasokol.repo.detail

import arrow.core.Either
import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.core.error.NetworkError
import com.matijasokol.repo.domain.DateUtils
import com.matijasokol.repo.domain.model.Repo
import javax.inject.Inject

class RepoDetailsUiMapper @Inject constructor(
    private val dictionary: Dictionary,
) {

    fun toUiState(
        isLoading: Boolean,
        repoOrError: Either<NetworkError, Repo>,
        repoFullName: String,
        authorImageUrl: String,
    ) = when (isLoading) {
        true -> RepoDetailState.Loading(
            repoFullName = repoFullName,
            authorImageUrl = authorImageUrl,
        )
        false -> when (repoOrError) {
            is Either.Left -> RepoDetailState.Error(
                errorMessage = dictionary.getString(R.string.repo_detail_message_cache_error),
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
            )
            is Either.Right -> RepoDetailState.Success(
                repo = repoOrError.value,
                info = buildInfoData(repoOrError.value),
                detailsButtonText = dictionary.getString(R.string.repo_detail_btn_repo_details),
                followersCountText = repoOrError.value.author.followersCount?.let {
                    dictionary.getString(R.string.repo_detail_followers_count_text, it)
                },
                reposCountText = repoOrError.value.author.reposCount?.let {
                    dictionary.getString(R.string.repo_detail_repos_count_text, it)
                },
                repoFullName = repoFullName,
                authorImageUrl = authorImageUrl,
            )
        }
    }

    private fun buildInfoData(repo: Repo): List<String> = with(dictionary) {
        listOf(
            getString(R.string.repo_detail_panel_watchers, repo.watchersCount),
            getString(R.string.repo_detail_panel_issues, repo.issuesCount),
            getString(R.string.repo_detail_panel_forks, repo.forksCount),
            getString(R.string.repo_detail_panel_starts, repo.starsCount),
            getString(R.string.repo_detail_panel_language, repo.language.orEmpty()),
            getString(R.string.repo_detail_panel_description, repo.description.orEmpty()),
            getString(
                R.string.repo_detail_panel_updated,
                DateUtils.dateToLocalDateString(repo.lastUpdated),
            ),
        )
    }
}
