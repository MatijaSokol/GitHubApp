package com.matijasokol.githubapp.navigation

import com.matijasokol.core.dictionary.Dictionary
import com.matijasokol.githubapp.R
import com.matijasokol.githubapp.navigation.NavigationError.DetailsUnavailable
import javax.inject.Inject

class NavigationErrorMapper @Inject constructor(private val dictionary: Dictionary) {

    fun map(error: NavigationError): String = when (error) {
        DetailsUnavailable -> dictionary.getString(R.string.mode_checker_navigation_disabled_message)
    }
}
