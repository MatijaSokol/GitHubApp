plugins {
    `java-test-fixtures`
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    testFixturesApi(projects.repo.datasourceTest)
    testFixturesApi(projects.core)
}
