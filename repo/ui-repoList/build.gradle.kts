apply {
    from("$rootDir/android-library-build.gradle")
}

plugins {
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.matijasokol.ui_repolist"
}

dependencies {
    "implementation"(projects.core)
    "implementation"(projects.components)
    "implementation"(projects.repo.repoDomain)

    "implementation"(libs.sqldelight.driver.android)

    "implementation"(libs.coil.compose)
    "implementation"(libs.material.icons)

    "androidTestImplementation"(projects.repo.repoDatasourceTest)
    "androidTestImplementation"(libs.compose.junit4)
    "androidTestImplementation"(libs.junit)
    "debugImplementation"(libs.compose.ui.test.manifest)
}