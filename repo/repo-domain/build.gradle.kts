plugins {
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    implementation(projects.core)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.javax.inject)

    testImplementation(projects.repo.repoDatasourceTest)
    testImplementation(libs.junit)

    testImplementation(project.dependencies.platform(libs.ktor.bom))
    testImplementation(libs.ktor.serialization.kotlinx.json)
}