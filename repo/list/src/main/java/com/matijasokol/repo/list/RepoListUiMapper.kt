package com.matijasokol.repo.list

import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class RepoListUiMapper @Inject constructor(private val dictionary: Dictionary) {

    private data class ListStaticData(val text: RepoListText)

    private val staticData by lazy { ListStaticData(text = mapText()) }

    fun initialState(query: String) = RepoListState(query = query, text = staticData.text)

    fun toUiState(
        loadState: Paginator.LoadState,
        items: List<Repo>,
        query: String,
        repoSortType: RepoSortType,
    ) = RepoListState(
        loadState = loadState,
        items = items.map(Repo::toRepoListItem).toPersistentList(),
        query = query,
        repoSortType = repoSortType,
        text = staticData.text,
    )

    private fun mapText(): RepoListText {
        val ascending = dictionary.getString(R.string.repo_list_sort_ascending)
        val descending = dictionary.getString(R.string.repo_list_sort_descending)

        return RepoListText(
            headerTitle = dictionary.getString(R.string.repo_list_title),
            headerSubtitle = dictionary.getString(R.string.repo_list_subtitle),
            searchPlaceholder = dictionary.getString(R.string.repo_list_query_label),
            searchIconContentDescription = dictionary.getString(R.string.repo_list_search_content_description),
            clearSearchButtonContentDescription = dictionary.getString(
                R.string.repo_list_clear_search_content_description,
            ),
            refreshErrorTitle = dictionary.getString(R.string.repo_list_refresh_error_title),
            loadErrorMessage = dictionary.getString(R.string.repo_list_message_error),
            profileBrowserErrorMessage = dictionary.getString(R.string.repo_list_message_browser_error),
            retryButtonText = dictionary.getString(R.string.repo_list_retry_text),
            sortOptions = RepoSortText(
                sortOptionsContentDescription = dictionary.getString(
                    R.string.repo_list_sort_options_content_description,
                ),
                starsOption = mapSortOption(R.string.repo_list_sort_stars, ascending, descending),
                forksOption = mapSortOption(R.string.repo_list_sort_forks, ascending, descending),
                updatedOption = mapSortOption(R.string.repo_list_sort_updated, ascending, descending),
            ),
            watchersIconContentDescription = dictionary.getString(R.string.repo_list_watchers_content_description),
            forksIconContentDescription = dictionary.getString(R.string.repo_list_forks_content_description),
            issuesIconContentDescription = dictionary.getString(R.string.repo_list_issues_content_description),
        )
    }

    private fun mapSortOption(labelResId: Int, ascending: String, descending: String): RepoSortOptionText {
        val label = dictionary.getString(labelResId)
        return RepoSortOptionText(
            displayLabel = label,
            ascendingActionContentDescription = dictionary.getString(
                R.string.repo_list_sort_direction_content_description,
                label,
                ascending,
            ),
            descendingActionContentDescription = dictionary.getString(
                R.string.repo_list_sort_direction_content_description,
                label,
                descending,
            ),
        )
    }
}
