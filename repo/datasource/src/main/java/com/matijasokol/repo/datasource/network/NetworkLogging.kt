package com.matijasokol.repo.datasource.network

import com.matijasokol.core.logging.AppLogger
import com.matijasokol.core.logging.LogTag
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders

/**
 * Installs Ktor's [Logging] plugin and sends every request and response, bodies included ([LogLevel.ALL]),
 * to [networkLogger], which writes them under [LogTag.NETWORK]. Filter Logcat with `tag:Network` to see them.
 *
 * Values of [SENSITIVE_HEADERS] (e.g. `Authorization`) are logged as `***`.
 *
 * Install this only when logging is wanted (debug builds, see `AppBuildConfig.isDebug`). Release builds skip the
 * plugin entirely instead of installing it with [LogLevel.NONE].
 *
 * Logcat truncates a single entry at roughly 4 KB. Timber's `DebugTree` splits longer messages into several
 * consecutive entries, so a large search response shows up as many `Network` lines, and its single-line JSON
 * body may be cut mid-value between two of them.
 */
internal fun HttpClientConfig<*>.installNetworkLogging(networkLogger: Logger) {
    install(Logging) {
        logger = networkLogger
        level = LogLevel.ALL
        sanitizeHeader { header -> SENSITIVE_HEADERS.any { it.equals(header, ignoreCase = true) } }
    }
}

/**
 * Bridges Ktor's [Logger] to [AppLogger], tagging every line with [LogTag.NETWORK].
 */
internal class NetworkLogger(private val appLogger: AppLogger) : Logger {

    override fun log(message: String) {
        appLogger.d(message, tag = LogTag.NETWORK)
    }
}

/**
 * Headers whose values are replaced with `***` in network logs, so credentials never end up in Logcat:
 * - `Authorization`: API token or credentials sent to GitHub (e.g. `token ghp_...`).
 * - `Proxy-Authorization`: credentials for a proxy between the device and GitHub.
 * - `Cookie` / `Set-Cookie`: session cookies sent and received.
 *
 * Header names are matched case-insensitively, because HTTP header names are case-insensitive and engines differ
 * in how they report them. The header name itself is still logged, so you can see that it was sent.
 */
private val SENSITIVE_HEADERS = listOf(
    HttpHeaders.Authorization,
    HttpHeaders.ProxyAuthorization,
    HttpHeaders.Cookie,
    HttpHeaders.SetCookie,
)
