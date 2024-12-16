apply {
    from("$rootDir/android-library-build.gradle")
}

plugins {
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.matijasokol.components"
}

dependencies {
    implementation(projects.core)
    implementation(libs.coil.compose)
}