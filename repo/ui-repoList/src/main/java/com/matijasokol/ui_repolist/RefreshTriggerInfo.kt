package com.matijasokol.ui_repolist

class RefreshTriggerInfo(
    val refreshTrigger: RefreshTrigger = RefreshTrigger.Initial,
    val query: String = DEFAULT_QUERY
) {

    fun copy(
        refreshTrigger: RefreshTrigger = this.refreshTrigger,
        query: String = this.query
    ): RefreshTriggerInfo {
        return RefreshTriggerInfo(
            refreshTrigger = refreshTrigger,
            query = query
        )
    }

    operator fun component1() = this.refreshTrigger

    operator fun component2() = this.query
}