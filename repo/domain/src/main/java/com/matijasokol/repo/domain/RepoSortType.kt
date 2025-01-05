package com.matijasokol.repo.domain

import com.matijasokol.core.domain.SortOrder

sealed class RepoSortType(
    open val order: SortOrder,
) {

    data class Stars(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order) {

        companion object {
            const val name: String = "Name"
        }
    }

    data class Forks(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order) {

        companion object {
            const val name: String = "Forks"
        }
    }

    data class Updated(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = order) {

        companion object {
            const val name: String = "Updated"
        }
    }

    data class Unknown(
        override val order: SortOrder = SortOrder.Descending,
    ) : RepoSortType(order = SortOrder.Descending) {

        companion object {
            const val name: String = "Unknown"
        }
    }
}
