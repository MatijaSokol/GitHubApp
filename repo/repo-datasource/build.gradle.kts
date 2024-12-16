apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
}

dependencies {
    "implementation"(projects.repo.repoDomain)
    "implementation"(projects.core)

    "implementation"(project.dependencies.platform(libs.ktor.bom))
    "implementation"(libs.bundles.ktor)

    "implementation"(libs.javax.inject)
}

sqldelight {
    // this will be the name of the generated database class
    databases.create("RepoDatabase") {
        // package name used for the database class
        packageName.set("com.matijasokol.repo_datasource.cache")
    }
}