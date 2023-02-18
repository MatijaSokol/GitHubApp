package com.matijasokol.core.domain

sealed interface SortOrder {

    object Ascending : SortOrder

    object Descending : SortOrder
}