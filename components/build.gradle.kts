plugins {
    alias(libs.plugins.githubapp.library)
    alias(libs.plugins.githubapp.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.matijasokol.components"
}

dependencies {
    implementation(projects.core)
    implementation(libs.coil.compose)
}