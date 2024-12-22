package com.matijasokol.core.domain

sealed interface SortOrder {

    data object Ascending : SortOrder

    data object Descending : SortOrder
}
