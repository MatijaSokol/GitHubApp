package com.matijasokol.core.logging

/**
 * Project-wide logging abstraction, used by every module (Android and pure Kotlin/JVM alike) instead of
 * calling Timber directly. The `app` module provides the Timber-backed implementation through Hilt.
 *
 * Pass a [LogTag] to make logs filterable in Logcat; without one, the implementation's default tag is used.
 */
interface AppLogger {

    fun v(message: String, throwable: Throwable? = null, tag: LogTag? = null)

    fun d(message: String, throwable: Throwable? = null, tag: LogTag? = null)

    fun i(message: String, throwable: Throwable? = null, tag: LogTag? = null)

    fun w(message: String, throwable: Throwable? = null, tag: LogTag? = null)

    fun e(message: String, throwable: Throwable? = null, tag: LogTag? = null)
}
