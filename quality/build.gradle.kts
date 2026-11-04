// build-logic is an included build and can't apply its own `githubapp.quality` plugin, so this project applies it to
// the build-logic sources instead. `./gradlew ktlintCheck detekt` (and ktlintFormat) then cover the convention plugins
// too, with reports in quality/build/reports.
//
// Not applied in the root project on purpose: that would put build-logic on the root classpath, and every module's
// versioned `alias(libs.plugins.githubapp.*)` request would fail because the plugin is already on the classpath.
plugins {
    alias(libs.plugins.githubapp.quality)
}

githubappQuality {
    kotlinSources.setFrom(
        fileTree(rootDir.resolve("build-logic")) {
            include("**/*.kt", "**/*.kts")
            exclude("**/build/**", "**/.gradle/**", "**/.kotlin/**")
        },
    )
}
