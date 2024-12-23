plugins {
    `java-test-fixtures`
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    testFixturesApi(libs.junit)
    testFixturesApi(projects.repo.datasourceTest)
    testFixturesApi(projects.core)
    testFixturesApi(libs.junit)
    testFixturesApi(libs.kotlinx.coroutines.test)
    testFixturesApi(libs.mockk)
    testFixturesApi(libs.kluent)
    testFixturesApi(libs.turbine)
}
