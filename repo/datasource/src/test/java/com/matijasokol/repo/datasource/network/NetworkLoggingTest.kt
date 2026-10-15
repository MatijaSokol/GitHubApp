package com.matijasokol.repo.datasource.network

import com.matijasokol.core.logging.LogTag
import com.matijasokol.repo.datasourcetest.logging.AppLoggerFake
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.junit.jupiter.api.Test

class NetworkLoggingTest {

    private val appLogger = AppLoggerFake()
    private val sut = NetworkLogger(appLogger)

    @Test
    fun `should FORWARD message to AppLogger under NETWORK tag`() {
        sut.log("GET /search/repositories")

        appLogger.entries shouldBeEqualTo listOf(
            AppLoggerFake.Entry(
                level = AppLoggerFake.Level.D,
                message = "GET /search/repositories",
                throwable = null,
                tag = LogTag.NETWORK,
            ),
        )
    }

    @Test
    fun `should MASK sensitive header values`() = runTest {
        val client = HttpClient(
            MockEngine {
                respond(
                    content = "{}",
                    headers = headersOf(
                        HttpHeaders.SetCookie to listOf("session=resp456"),
                        "X-Visible-Response" to listOf("visible-response-value"),
                    ),
                )
            },
        ) {
            installNetworkLogging(sut)
        }

        client.get("https://api.github.com/search/repositories") {
            header(HttpHeaders.Authorization, "token ghp_secret")
            header(HttpHeaders.ProxyAuthorization, "Basic proxy_secret")
            header(HttpHeaders.Cookie, "session=abc123")
            header("X-Visible", "visible-request-value")
        }.bodyAsText()

        val logged = appLogger.messages.joinToString("\n")
        logged shouldContain "visible-request-value" // proves request headers were logged at all
        logged shouldContain "visible-response-value" // proves response headers were logged at all
        logged shouldNotContain "ghp_secret"
        logged shouldNotContain "proxy_secret"
        logged shouldNotContain "session=abc123"
        logged shouldNotContain "session=resp456"
    }
}
