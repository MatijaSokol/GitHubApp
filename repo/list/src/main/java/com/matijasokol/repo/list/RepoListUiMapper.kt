package com.matijasokol.repo.list

import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class RepoListUiMapper @Inject constructor(
    private val dictionary: Dictionary,
) {

    private data class ListStaticData(
        val errorText: String,
        val retryButtonText: String,
    )

    private val staticData by lazy {
        ListStaticData(
            errorText = dictionary.getString(R.string.repo_list_message_error),
            retryButtonText = dictionary.getString(R.string.repo_list_retry_text),
        )
    }

    fun toUiState(
        loadState: Paginator.LoadState,
        items: List<Repo>,
        query: String,
        sortMenuVisible: Boolean,
        repoSortType: RepoSortType,
    ) = RepoListState(
        loadState = loadState,
        items = items.map(Repo::toRepoListItem).toPersistentList(),
        query = query,
        queryLabel = dictionary.getString(R.string.repo_list_query_label),
        sortMenuVisible = sortMenuVisible,
        sortMenuOptions = with(dictionary) {
            persistentListOf(
                Pair(RepoSortType.Stars(SortOrder.Ascending), getString(R.string.repo_list_sort_stars_asc)),
                Pair(RepoSortType.Stars(SortOrder.Descending), getString(R.string.repo_list_sort_stars_desc)),
                Pair(RepoSortType.Forks(SortOrder.Ascending), getString(R.string.repo_list_sort_forks_asc)),
                Pair(RepoSortType.Forks(SortOrder.Descending), getString(R.string.repo_list_sort_forks_desc)),
                Pair(RepoSortType.Updated(SortOrder.Ascending), getString(R.string.repo_list_sort_updated_asc)),
                Pair(RepoSortType.Updated(SortOrder.Descending), getString(R.string.repo_list_sort_updated_desc)),
            )
        },
        repoSortType = repoSortType,
        errorText = staticData.errorText,
        retryButtonText = staticData.retryButtonText,
    )
}
