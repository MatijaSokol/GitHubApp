package com.matijasokol.repo.list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo.domain.Paginator.LoadState.Append
import com.matijasokol.repo.domain.Paginator.LoadState.AppendError
import com.matijasokol.repo.domain.Paginator.LoadState.Loaded
import com.matijasokol.repo.domain.Paginator.LoadState.Refresh
import com.matijasokol.repo.domain.Paginator.LoadState.RefreshError
import com.matijasokol.repo.domain.RepoSortType
import kotlinx.collections.immutable.persistentListOf

internal class RepoListStatePreviewParameterProvider : PreviewParameterProvider<RepoListState> {

    override val values = RepoListPreviewFixtures.states

    override fun getDisplayName(index: Int): String? = when (index) {
        0 -> "Loading"
        1 -> "Success"
        2 -> "Error"
        3 -> "Success - Loading More"
        4 -> "Success - Load More Error"
        5 -> "Success - Empty Search Results"
        6 -> "Success - Long Content"
        else -> super.getDisplayName(index)
    }
}

internal object RepoListPreviewFixtures {
    val text = RepoListText(
        headerTitle = "Discover",
        headerSubtitle = "Repositories worth exploring",
        searchPlaceholder = "Search repositories",
        searchIconContentDescription = "Search",
        clearSearchButtonContentDescription = "Clear search",
        refreshErrorTitle = "Couldn't load repositories",
        loadErrorMessage = "Check your connection and try again.",
        profileBrowserErrorMessage = "Cannot open the author profile.",
        retryButtonText = "Retry",
        sortOptions = RepoSortText(
            sortOptionsContentDescription = "Sort options",
            starsOption = sortOption("Stars"),
            forksOption = sortOption("Forks"),
            updatedOption = sortOption("Updated"),
        ),
    )

    val regularItem = RepoListItem(
        id = 1,
        fullName = "JetBrains/kotlin",
        name = "kotlin",
        authorName = "JetBrains",
        authorImageUrl = "",
        authorProfileUrl = "https://github.com/JetBrains",
        stars = "49.6k",
        starsContentDescription = "Stars: 49640",
        forks = "5.8k",
        forksContentDescription = "Forks: 5805",
        watchers = "49.6k",
        watchersContentDescription = "Watchers: 49640",
    )

    val longItem = RepoListItem(
        id = 2,
        fullName = "androidx/androidx-compose-material3-adaptive-navigation-suite-experimental",
        name = "androidx-compose-material3-adaptive-navigation-suite-experimental",
        authorName = "androidx-with-an-unusually-long-organization-name",
        authorImageUrl = "",
        authorProfileUrl = "https://github.com/androidx",
        stars = "2.1b",
        starsContentDescription = "Stars: 2147483647",
        forks = "987.7m",
        forksContentDescription = "Forks: 987654321",
        watchers = "2.1b",
        watchersContentDescription = "Watchers: 2147483647",
    )

    val loaded = RepoListState(
        loadState = Loaded,
        items = persistentListOf(
            regularItem,
            regularItem.copy(id = 3, fullName = "square/okhttp", name = "okhttp", authorName = "square"),
            longItem,
            regularItem.copy(id = 4, fullName = "coil-kt/coil", name = "coil", authorName = "coil-kt"),
        ),
        repoSortType = RepoSortType.Stars(SortOrder.Descending),
        text = text,
    )

    val longContent = RepoListState(
        loadState = Loaded,
        items = persistentListOf(longItem),
        query = "adaptive navigation",
        repoSortType = RepoSortType.Updated(SortOrder.Ascending),
        text = text,
    )

    private val loading = RepoListState(
        loadState = Refresh,
        query = "kotlin",
        text = text,
    )

    private val error = RepoListState(
        loadState = RefreshError,
        text = text,
    )

    private val loadingMore = loaded.copy(
        loadState = Append,
        items = persistentListOf(regularItem),
    )

    private val loadMoreError = loaded.copy(
        loadState = AppendError,
        items = persistentListOf(regularItem),
    )

    private val emptySearchResults = loaded.copy(
        items = persistentListOf(),
        query = "no-results",
    )

    val states = sequenceOf(
        loading,
        loaded,
        error,
        loadingMore,
        loadMoreError,
        emptySearchResults,
        longContent,
    )

    private fun sortOption(label: String) = RepoSortOptionText(
        displayLabel = label,
        ascendingActionContentDescription = "$label ascending",
        descendingActionContentDescription = "$label descending",
    )
}
