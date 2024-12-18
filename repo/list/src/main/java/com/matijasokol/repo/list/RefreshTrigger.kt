package com.matijasokol.repo.list

sealed interface RefreshTrigger {

    data object Query : RefreshTrigger

    data object PullToRefresh : RefreshTrigger

    data object NextPage : RefreshTrigger

    data object Initial : RefreshTrigger
}
