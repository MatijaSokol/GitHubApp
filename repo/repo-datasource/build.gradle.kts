apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    kotlin("kapt")
}

dependencies {
    "implementation"(project(Modules.repoDomain))
    "implementation"(project(Modules.core))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)
    "implementation"(Ktor.contentNegotiation)
    "implementation"(Ktor.json)
    "implementation"(Ktor.logging)

    "implementation"(Javax.inject)
}