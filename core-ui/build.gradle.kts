plugins {
    alias(libs.plugins.githubapp.library)
    alias(libs.plugins.githubapp.library.compose)
}

android {
    namespace = "com.matijasokol.coreui"
}

dependencies {
    implementation(projects.core)

    implementation(libs.kotlinx.coroutines)

    implementation(libs.javax.inject)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    api(libs.navigation3.runtime)
    api(libs.navigation3.ui)

    implementation(libs.kotlinx.serialization)
    implementation(libs.lifecycle.viewmodel)
}
