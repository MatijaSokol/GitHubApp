package com.matijasokol.repo.domain

import com.matijasokol.core.domain.SortOrder

sealed class RepoSortType(
    open val order: SortOrder,
) {

    data class Stars(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order)

    data class Forks(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order)

    data class Updated(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order)

    data class Unknown(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order)
}
