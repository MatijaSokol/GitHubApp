package com.matijasokol.repodatasourcetest.network

import com.matijasokol.repodatasource.BasicPaginator
import com.matijasokol.repodomain.Paginator
import com.matijasokol.repodomain.RepoCache
import com.matijasokol.repodomain.RepoService

class FakePaginator(
    private val repoService: RepoService,
    private val repoCache: RepoCache,
) : Paginator by BasicPaginator(repoService, repoCache)
