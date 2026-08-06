package com.matijasokol.repo.list

import androidx.compose.runtime.Stable
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class RepoListState(
    val loadState: Paginator.LoadState = Paginator.LoadState.Refresh,
    val items: ImmutableList<RepoListItem> = persistentListOf(),
    val query: String = "",
    val repoSortType: RepoSortType = RepoSortType.Unknown(),
    val text: RepoListText = RepoListText(),
)

data class RepoListItem(
    val id: Int,
    val fullName: String,
    val name: String,
    val authorName: String,
    val authorImageUrl: String,
    val authorProfileUrl: String,
    val watchers: String,
    val forks: String,
    val issues: String,
)

data class RepoListText(
    val headerTitle: String = "",
    val headerSubtitle: String = "",
    val searchPlaceholder: String = "",
    val searchIconContentDescription: String = "",
    val clearSearchButtonContentDescription: String = "",
    val refreshErrorTitle: String = "",
    val loadErrorMessage: String = "",
    val profileBrowserErrorMessage: String = "",
    val retryButtonText: String = "",
    val sortOptions: RepoSortText = RepoSortText(),
    val watchersIconContentDescription: String = "",
    val forksIconContentDescription: String = "",
    val issuesIconContentDescription: String = "",
)

data class RepoSortText(
    val sortOptionsContentDescription: String = "",
    val starsOption: RepoSortOptionText = RepoSortOptionText(),
    val forksOption: RepoSortOptionText = RepoSortOptionText(),
    val updatedOption: RepoSortOptionText = RepoSortOptionText(),
)

data class RepoSortOptionText(
    val displayLabel: String = "",
    val ascendingActionContentDescription: String = "",
    val descendingActionContentDescription: String = "",
)

fun Repo.toRepoListItem() = RepoListItem(
    id = id,
    fullName = fullName,
    name = name,
    authorName = author.name,
    authorImageUrl = author.image,
    authorProfileUrl = author.profileUrl,
    watchers = watchersCount.toString(),
    forks = forksCount.toString(),
    issues = issuesCount.toString(),
)
