package quality

import org.gradle.api.file.FileCollection
import org.gradle.process.CommandLineArgumentProvider

/**
 * The files ktlint checks or formats, as absolute paths. Resolved when the task runs, so a later
 * `githubappQuality { kotlinSources.setFrom(...) }` is always picked up.
 */
class KtlintSourceArguments(private val sources: FileCollection) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> = sources.files.sorted().map { it.absolutePath }
}
