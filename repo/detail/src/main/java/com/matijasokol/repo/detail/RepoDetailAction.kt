package com.matijasokol.repo.detail

import com.matijasokol.coreui.text.UiText

sealed interface RepoDetailAction {

    data class ShowMessage(val message: UiText) : RepoDetailAction
}
