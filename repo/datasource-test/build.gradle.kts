plugins {
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    implementation(projects.repo.datasource)
    implementation(projects.repo.domain)
    implementation(projects.core)

    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)
}
