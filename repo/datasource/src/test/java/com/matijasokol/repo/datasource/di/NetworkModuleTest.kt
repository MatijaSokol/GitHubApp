package com.matijasokol.repo.datasource.di

import com.matijasokol.core.buildconfig.AppBuildConfig
import com.matijasokol.repo.datasource.network.json
import com.matijasokol.repo.datasourcetest.logging.AppLoggerFake
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.pluginOrNull
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

class NetworkModuleTest {

    @Test
    fun `should INSTALL logging plugin WHEN build is debug`() {
        buildClient(isDebug = true).use { it.pluginOrNull(Logging).shouldNotBeNull() }
    }

    @Test
    fun `should NOT INSTALL logging plugin WHEN build is release`() {
        buildClient(isDebug = false).use { it.pluginOrNull(Logging).shouldBeNull() }
    }

    private fun buildClient(isDebug: Boolean) = NetworkModule.provideHttpClient(
        json = json,
        engine = OkHttp, // building the client makes no network call
        appBuildConfig = AppBuildConfig(
            isDebug = isDebug,
            applicationId = "com.matijasokol.githubapp",
            buildType = if (isDebug) "debug" else "release",
            flavor = "devFree",
            versionName = "1.0.0",
            versionCode = 1,
        ),
        appLogger = AppLoggerFake(),
    )
}
