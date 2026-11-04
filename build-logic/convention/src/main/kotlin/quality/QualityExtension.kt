package quality

import org.gradle.api.file.ConfigurableFileCollection

/** The `githubappQuality { }` extension added by `githubapp.quality`. */
interface QualityExtension {

    /**
     * Kotlin files and scripts checked by detekt and ktlint. Defaults to every `*.kt` and `*.kts` file in every source
     * set under the module's `src` directory; use `setFrom` to check other files instead.
     */
    val kotlinSources: ConfigurableFileCollection
}
