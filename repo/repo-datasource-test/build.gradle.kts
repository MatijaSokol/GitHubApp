apply {
    from("$rootDir/library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.repoDataSource))
    "implementation"(project(Modules.repoDomain))
    "implementation"(project(Modules.core))

    "implementation"(Ktor.ktorClientMock)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.contentNegotiation)
    "implementation"(Ktor.json)
}