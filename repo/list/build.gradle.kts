plugins {
    alias(libs.plugins.githubapp.library)
    alias(libs.plugins.githubapp.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.matijasokol.repo.list"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreUi)
    implementation(projects.repo.repoDomain)

    implementation(libs.sqldelight.driver.android)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.material.icons)

    androidTestImplementation(projects.repo.repoDatasourceTest)
    androidTestImplementation(libs.compose.junit4)
    androidTestImplementation(libs.junit)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
