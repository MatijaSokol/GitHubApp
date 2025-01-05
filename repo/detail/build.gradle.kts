plugins {
    alias(libs.plugins.githubapp.library)
    alias(libs.plugins.githubapp.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.matijasokol.repo.detail"

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreUi)
    implementation(projects.repo.domain)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.compose.navigation)

    testImplementation(testFixtures(projects.test))
    testImplementation(projects.repo.datasourceTest)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.repo.datasourceTest)
    androidTestImplementation(libs.compose.junit4)
    androidTestImplementation(libs.junit)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.activity.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.arrow.core)
    implementation(libs.arrow.coroutines)

    implementation(libs.kotlinx.collections)
}
