plugins {
    `java-test-fixtures`
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    testFixturesApi(projects.repo.datasourceTest)
    testFixturesApi(projects.core)

    // compileOnly: every consumer (feature unit tests) already brings JUnit and coroutines-test,
    // so the fixtures don't impose their own copies or versions on consumers' runtime classpaths.
    testFixturesCompileOnly(platform(libs.junit.bom))
    testFixturesCompileOnly(libs.junit.jupiter)
    testFixturesCompileOnly(libs.kotlinx.coroutines.test)
}
