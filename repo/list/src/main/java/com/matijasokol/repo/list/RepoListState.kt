package com.matijasokol.repo.list

import androidx.compose.runtime.Stable
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
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
    val stars: String,
    val starsContentDescription: String,
    val forks: String,
    val forksContentDescription: String,
    val watchers: String,
    val watchersContentDescription: String,
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
