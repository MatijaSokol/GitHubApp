package com.matijasokol.githubapp.logging

import com.matijasokol.core.logging.AppLogger
import com.matijasokol.core.logging.LogTag
import timber.log.Timber
import javax.inject.Inject

/**
 * Timber-backed [AppLogger] used for logging across the whole project, in both Android and JVM modules.
 * Logs are dropped in release builds, where no Timber tree is planted.
 */
class TimberLogger @Inject constructor() : AppLogger {

    override fun v(message: String, throwable: Throwable?, tag: LogTag?) {
        tree(tag).v(throwable, message)
    }

    override fun d(message: String, throwable: Throwable?, tag: LogTag?) {
        tree(tag).d(throwable, message)
    }

    override fun i(message: String, throwable: Throwable?, tag: LogTag?) {
        tree(tag).i(throwable, message)
    }

    override fun w(message: String, throwable: Throwable?, tag: LogTag?) {
        tree(tag).w(throwable, message)
    }

    override fun e(message: String, throwable: Throwable?, tag: LogTag?) {
        tree(tag).e(throwable, message)
    }

    private fun tree(tag: LogTag?): Timber.Tree {
        val explicitTag = tag?.value
        return Timber.tag(if (explicitTag.isNullOrEmpty()) DEFAULT_TAG else explicitTag)
    }
}

private const val DEFAULT_TAG = "AppLogger"
