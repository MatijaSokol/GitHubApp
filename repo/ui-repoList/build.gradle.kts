apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.components))
    "implementation"(project(Modules.repoDomain))

    "implementation"(SqlDelight.androidDriver)

    "implementation"(Coil.coil)

    "androidTestImplementation"(project(Modules.repoDataSourceTest))
    "androidTestImplementation"(ComposeTest.uiTestJunit4)
    "androidTestImplementation"(Junit.junit4)
    "debugImplementation"(ComposeTest.uiTestManifest)
}