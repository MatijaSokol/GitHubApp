package com.matijasokol.repo.datasourcetest.logging

import com.matijasokol.core.logging.AppLogger
import com.matijasokol.core.logging.LogTag

class AppLoggerFake : AppLogger {

    data class Entry(
        val level: Level,
        val message: String,
        val throwable: Throwable?,
        val tag: LogTag?,
    )

    enum class Level { V, D, I, W, E }

    val entries: List<Entry>
        field = mutableListOf<Entry>()

    val messages: List<String> get() = entries.map { it.message }

    override fun v(message: String, throwable: Throwable?, tag: LogTag?) = record(Level.V, message, throwable, tag)

    override fun d(message: String, throwable: Throwable?, tag: LogTag?) = record(Level.D, message, throwable, tag)

    override fun i(message: String, throwable: Throwable?, tag: LogTag?) = record(Level.I, message, throwable, tag)

    override fun w(message: String, throwable: Throwable?, tag: LogTag?) = record(Level.W, message, throwable, tag)

    override fun e(message: String, throwable: Throwable?, tag: LogTag?) = record(Level.E, message, throwable, tag)

    private fun record(level: Level, message: String, throwable: Throwable?, tag: LogTag?) {
        entries += Entry(level, message, throwable, tag)
    }
}
