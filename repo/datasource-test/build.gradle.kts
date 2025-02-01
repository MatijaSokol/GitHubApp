plugins {
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    implementation(projects.core)
    implementation(projects.repo.domain)
    implementation(projects.repo.datasource)

    implementation(project.dependencies.platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)
}
