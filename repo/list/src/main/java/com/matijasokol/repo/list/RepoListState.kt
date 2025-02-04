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
    val queryLabel: String = "",
    val sortMenuVisible: Boolean = false,
    val sortMenuOptions: ImmutableList<Pair<RepoSortType, String>> = persistentListOf(),
    val repoSortType: RepoSortType = RepoSortType.Unknown(),
    val errorText: String = "",
    val retryButtonText: String = "",
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
