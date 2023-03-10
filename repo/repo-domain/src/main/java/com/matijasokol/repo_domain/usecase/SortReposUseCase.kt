package com.matijasokol.repo_domain.usecase

import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo_domain.RepoSortType
import com.matijasokol.repo_domain.model.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SortReposUseCase {

    suspend fun execute(
        repos: List<Repo>,
        repoSortType: RepoSortType
    ): List<Repo> = withContext(Dispatchers.Default) {
        when (repoSortType) {
            is RepoSortType.Forks -> when (repoSortType.order) {
                SortOrder.Ascending -> repos.sortedBy { it.forksCount }
                SortOrder.Descending -> repos.sortedByDescending { it.forksCount }
            }
            is RepoSortType.Stars -> when (repoSortType.order) {
                SortOrder.Ascending -> repos.sortedBy { it.starsCount }
                SortOrder.Descending -> repos.sortedByDescending { it.starsCount }
            }
            is RepoSortType.Updated -> when (repoSortType.order) {
                SortOrder.Ascending -> repos.sortedBy { it.lastUpdated }
                SortOrder.Descending -> repos.sortedByDescending { it.lastUpdated }
            }
            is RepoSortType.Unknown -> repos
        }
    }
}