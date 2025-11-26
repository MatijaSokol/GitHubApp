package com.matijasokol.githubapp.navigation

import com.matijasokol.core.mode.AppMode
import com.matijasokol.githubapp.ModeChecker
import javax.inject.Inject

class CanShowDetailsUseCase @Inject constructor(private val modeChecker: ModeChecker) {

    operator fun invoke(): Boolean = modeChecker.appMode == AppMode.Paid
}
