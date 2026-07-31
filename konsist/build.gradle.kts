import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.githubapp.jvm.library)
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kluent)
    testImplementation(libs.konsist)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test>().configureEach {
    // These tests inspect production sources across modules, which Gradle would not otherwise track as this module's inputs.
    outputs.upToDateWhen { false }
}
