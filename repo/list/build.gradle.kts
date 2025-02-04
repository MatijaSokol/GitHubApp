plugins {
    alias(libs.plugins.githubapp.library)
    alias(libs.plugins.githubapp.library.compose)
}

android {
    namespace = "com.matijasokol.repo.list"

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

    implementation(libs.sqldelight.driver.android)

    implementation(libs.kotlinx.collections)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    implementation(libs.arrow.core)
    implementation(libs.arrow.coroutines)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(testFixtures(projects.test))
    testImplementation(projects.repo.datasourceTest)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.repo.datasourceTest)
    androidTestImplementation(libs.compose.junit4)
    androidTestImplementation(libs.junit)
}
