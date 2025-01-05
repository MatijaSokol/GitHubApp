pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GitHubApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "app",
    "core",
    "core-ui",
    "repo",
    "repo:datasource",
    "repo:datasource-test",
    "repo:domain",
    "repo:list",
    "repo:detail",
    "test"
)