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
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(project.dependencies.platform(libs.ktor.bom))
    testImplementation(libs.ktor.serialization.kotlinx.json)
}
