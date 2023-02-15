package com.matijasokol.repo_datasource_test.network

import com.matijasokol.repo_datasource.BasicPaginator
import com.matijasokol.repo_domain.Paginator
import com.matijasokol.repo_domain.RepoService

class FakePaginator(
    private val repoService: RepoService
) : Paginator by BasicPaginator(repoService)