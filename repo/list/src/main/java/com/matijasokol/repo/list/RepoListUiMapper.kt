package com.matijasokol.repo.list

import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo
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
        sortMenuVisible = sortMenuVisible,
        repoSortType = repoSortType,
        errorText = staticData.errorText,
        retryButtonText = staticData.retryButtonText,
    )
}
