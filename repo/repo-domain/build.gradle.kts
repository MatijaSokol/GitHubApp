apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(projects.core)
    "implementation"(libs.kotlinx.coroutines)
    "implementation"(libs.javax.inject)

    "testImplementation"(projects.repo.repoDatasourceTest)
    "testImplementation"(libs.junit)

    "testImplementation"(project.dependencies.platform(libs.ktor.bom))
    "testImplementation"(libs.ktor.serialization.kotlinx.json)
}