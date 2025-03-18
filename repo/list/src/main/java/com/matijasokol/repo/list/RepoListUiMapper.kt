package com.matijasokol.repo.list

import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.core.domain.SortOrder.Ascending
import com.matijasokol.core.domain.SortOrder.Descending
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.RepoSortType.Forks
import com.matijasokol.repo.domain.RepoSortType.Stars
import com.matijasokol.repo.domain.RepoSortType.Updated
import com.matijasokol.repo.domain.model.Repo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class RepoListUiMapper @Inject constructor(
    private val dictionary: Dictionary,
) {

    private data class ListStaticData(
        val errorText: String,
        val retryButtonText: String,
        val sortMenuOptions: ImmutableList<Pair<RepoSortType, String>>,
    )

    private val staticData by lazy {
        ListStaticData(
            errorText = dictionary.getString(R.string.repo_list_message_error),
            retryButtonText = dictionary.getString(R.string.repo_list_retry_text),
            sortMenuOptions = with(dictionary) {
                persistentListOf(
                    Pair(Stars(Ascending), getString(R.string.repo_list_sort_stars_asc)),
                    Pair(Stars(Descending), getString(R.string.repo_list_sort_stars_desc)),
                    Pair(Forks(Ascending), getString(R.string.repo_list_sort_forks_asc)),
                    Pair(Forks(Descending), getString(R.string.repo_list_sort_forks_desc)),
                    Pair(Updated(Ascending), getString(R.string.repo_list_sort_updated_asc)),
                    Pair(Updated(Descending), getString(R.string.repo_list_sort_updated_desc)),
                )
            },
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
        sortMenuOptions = staticData.sortMenuOptions,
        repoSortType = repoSortType,
        errorText = staticData.errorText,
        retryButtonText = staticData.retryButtonText,
    )
}
