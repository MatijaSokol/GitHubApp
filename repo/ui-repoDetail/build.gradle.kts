plugins {
    alias(libs.plugins.githubapp.library)
    alias(libs.plugins.githubapp.library.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.matijasokol.uirepodetail"
}

dependencies {
    implementation(projects.core)
    implementation(projects.components)
    implementation(projects.repo.repoDomain)

    implementation(libs.coil.compose)

    androidTestImplementation(projects.repo.repoDatasourceTest)
    androidTestImplementation(libs.compose.junit4)
    androidTestImplementation(libs.junit)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.activity.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
