pluginManagement {
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

rootProject.name = "GitHub App"

include("app")
include("core")
include("components")
include("repo")
include("repo:repo-datasource")
include("repo:repo-datasource-test")
include("repo:repo-domain")
include("repo:ui-repoList")
include("repo:ui-repoDetail")