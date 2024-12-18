package com.matijasokol.repo.datasourcetest.network

import com.matijasokol.repo.datasource.BasicPaginator
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoCache
import com.matijasokol.repo.domain.RepoService

class FakePaginator(
    private val repoService: RepoService,
    private val repoCache: RepoCache,
) : Paginator by BasicPaginator(repoService, repoCache)
