package com.matijasokol.repo.domain.usecase

import com.matijasokol.core.dispatchers.AppDispatchers
import com.matijasokol.core.domain.SortOrder
import com.matijasokol.repo.domain.RepoSortType
import com.matijasokol.repo.domain.model.Repo
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SortReposUseCase @Inject constructor(private val appDispatchers: AppDispatchers) {

    suspend operator fun invoke(
        repos: List<Repo>,
        repoSortType: RepoSortType,
    ): List<Repo> = withContext(appDispatchers.default) {
        when (repoSortType) {
            is RepoSortType.Forks -> when (repoSortType.order) {
                SortOrder.Ascending -> repos.sortedBy(Repo::forksCount)
                SortOrder.Descending -> repos.sortedByDescending(Repo::forksCount)
            }
            is RepoSortType.Stars -> when (repoSortType.order) {
                SortOrder.Ascending -> repos.sortedBy(Repo::starsCount)
                SortOrder.Descending -> repos.sortedByDescending(Repo::starsCount)
            }
            is RepoSortType.Updated -> when (repoSortType.order) {
                SortOrder.Ascending -> repos.sortedBy(Repo::lastUpdated)
                SortOrder.Descending -> repos.sortedByDescending(Repo::lastUpdated)
            }
            is RepoSortType.Unknown -> repos
        }
    }
}
