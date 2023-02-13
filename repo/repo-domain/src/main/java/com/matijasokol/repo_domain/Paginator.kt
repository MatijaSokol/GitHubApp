package com.matijasokol.repo_domain

interface Paginator<Item> {

    suspend fun loadNext(query: String)

    fun reset()
}