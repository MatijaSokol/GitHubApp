package com.matijasokol.repo_domain

interface Paginator<Key, Item> {

    suspend fun loadNextItems()

    fun reset()
}