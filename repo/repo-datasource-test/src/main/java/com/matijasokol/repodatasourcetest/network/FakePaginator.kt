package com.matijasokol.repodatasourcetest.network

import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.RepoService
import com.matijasokol.repodatasource.BasicPaginator

class FakePaginator(
    private val repoService: RepoService,
    private val repoCache: RepoCache,
) : Paginator by BasicPaginator(repoService, repoCache)
