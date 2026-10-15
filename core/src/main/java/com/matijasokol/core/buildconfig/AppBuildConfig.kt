package com.matijasokol.core.buildconfig

/**
 * @property isDebug `true` for debuggable builds (the generated `DEBUG` build config flag).
 * @property applicationId e.g. `com.matijasokol.githubapp.free`.
 * @property buildType e.g. `debug` or `release`.
 * @property flavor combined product flavor name, e.g. `devPaid` or `prodFree`.
 * @property versionName e.g. `1.3.0.1`.
 * @property versionCode e.g. `1030001`.
 */
data class AppBuildConfig(
    val isDebug: Boolean,
    val applicationId: String,
    val buildType: String,
    val flavor: String,
    val versionName: String,
    val versionCode: Int,
)
