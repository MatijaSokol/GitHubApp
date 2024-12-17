plugins {
    alias(libs.plugins.githubapp.jvm.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.githubapp.sqldelight)
}

dependencies {
    implementation(projects.repo.repoDomain)
    implementation(projects.core)

    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)

    implementation(libs.javax.inject)
}
