package com.matijasokol.ui_repolist

sealed interface RefreshTrigger {

    object Query : RefreshTrigger

    object PullToRefresh : RefreshTrigger

    object NextPage : RefreshTrigger

    object Initial : RefreshTrigger
}