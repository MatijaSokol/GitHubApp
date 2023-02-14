apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))

    "implementation"(Kotlin.coroutines)

    "testImplementation"(project(Modules.repoDataSourceTest))
    "testImplementation"(Junit.junit4)
    "testImplementation"(Ktor.clientSerialization)
}