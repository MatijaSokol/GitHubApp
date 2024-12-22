package com.matijasokol.repo.datasourcetest.network

import com.matijasokol.repo.datasource.network.BasicPaginator
import com.matijasokol.repo.domain.Paginator
import com.matijasokol.repo.domain.RepoService

class FakePaginator(
    private val repoService: RepoService,
) : Paginator by BasicPaginator(repoService)
