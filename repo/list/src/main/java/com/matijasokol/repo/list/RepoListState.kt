package com.matijasokol.repo.list

import androidx.compose.runtime.Stable
import com.matijasokol.coreui.text.UiText
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
    val starsContentDescription: UiText,
    val forks: String,
    val forksContentDescription: UiText,
    val watchers: String,
    val watchersContentDescription: UiText,
)

data class RepoListText(
    val headerTitle: UiText = UiText.StringText(""),
    val headerSubtitle: UiText = UiText.StringText(""),
    val searchPlaceholder: UiText = UiText.StringText(""),
    val searchIconContentDescription: UiText = UiText.StringText(""),
    val clearSearchButtonContentDescription: UiText = UiText.StringText(""),
    val refreshErrorTitle: UiText = UiText.StringText(""),
    val emptyResultTitle: UiText = UiText.StringText(""),
    val emptyResultMessage: UiText = UiText.StringText(""),
    val loadErrorMessage: UiText = UiText.StringText(""),
    val retryButtonText: UiText = UiText.StringText(""),
    val sortOptions: RepoSortText = RepoSortText(),
)

data class RepoSortText(
    val sortOptionsContentDescription: UiText = UiText.StringText(""),
    val starsOption: RepoSortOptionText = RepoSortOptionText(),
    val forksOption: RepoSortOptionText = RepoSortOptionText(),
    val updatedOption: RepoSortOptionText = RepoSortOptionText(),
)

data class RepoSortOptionText(
    val displayLabel: UiText = UiText.StringText(""),
    val ascendingActionContentDescription: UiText = UiText.StringText(""),
    val descendingActionContentDescription: UiText = UiText.StringText(""),
)
