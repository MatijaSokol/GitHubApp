package com.matijasokol.githubapp

import com.matijasokol.core.mode.AppMode
import javax.inject.Inject

class ModeChecker @Inject constructor() {

    val appMode = AppMode.Paid
}
