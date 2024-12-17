plugins {
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    implementation(projects.repo.repoDatasource)
    implementation(projects.repo.repoDomain)
    implementation(projects.core)

    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)
}