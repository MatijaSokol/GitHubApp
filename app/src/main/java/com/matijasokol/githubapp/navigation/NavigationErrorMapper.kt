package com.matijasokol.githubapp.navigation

import com.matijasokol.coreui.text.UiText
import com.matijasokol.githubapp.R
import com.matijasokol.githubapp.navigation.NavigationError.DetailsUnavailable
import javax.inject.Inject

class NavigationErrorMapper @Inject constructor() {

    fun map(error: NavigationError): UiText = when (error) {
        DetailsUnavailable -> UiText.StringResource(R.string.mode_checker_navigation_disabled_message)
    }
}
