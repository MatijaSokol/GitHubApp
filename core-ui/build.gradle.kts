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

    implementation(libs.kotlinx.serialization)
    implementation(libs.lifecycle.viewmodel)
}
