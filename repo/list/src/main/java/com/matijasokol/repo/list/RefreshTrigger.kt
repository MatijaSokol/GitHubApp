package com.matijasokol.repo.list

sealed interface RefreshTrigger {

    object Query : RefreshTrigger

    object PullToRefresh : RefreshTrigger

    object NextPage : RefreshTrigger

    object Initial : RefreshTrigger
}
