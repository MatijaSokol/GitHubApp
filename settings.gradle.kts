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
    "components",
    "repo",
    "repo:repo-datasource",
    "repo:repo-datasource-test",
    "repo:repo-domain",
    "repo:ui-repoList",
    "repo:ui-repoDetail",
)