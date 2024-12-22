plugins {
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    implementation(projects.core)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.javax.inject)

    implementation(libs.arrow.core)
    implementation(libs.arrow.coroutines)

    testImplementation(projects.repo.datasourceTest)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)

    testImplementation(project.dependencies.platform(libs.ktor.bom))
    testImplementation(libs.ktor.serialization.kotlinx.json)
}
