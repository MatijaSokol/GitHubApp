package com.matijasokol.githubapp.logging

import timber.log.Timber

/**
 * Plants a [Timber.DebugTree] in debug builds only. Release builds plant no tree, so every Timber call is a no-op.
 * This is the only place in the project that plants a tree.
 */
fun initLogging(isDebug: Boolean) {
    if (isDebug) {
        Timber.plant(Timber.DebugTree())
    }
}
