package quality

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Runs [checker] on [sources], writes every violation to [reportFile] and fails if there are any.
 *
 * The report uses paths relative to [rootDirectory], so it is the same on every machine. The failure message uses
 * absolute `file://` URIs with a `:line` suffix (the format the Kotlin compiler uses), so terminals and the IDE
 * console turn each violation into a link that opens the file at that line.
 */
@CacheableTask
abstract class XmlFormatCheckTask : DefaultTask() {

  @get:InputFiles
  @get:SkipWhenEmpty
  @get:IgnoreEmptyDirectories
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val sources: ConfigurableFileCollection

  /** The rules to apply. `githubapp.quality` sets a default [XmlFormatChecker] as the convention. */
  @get:Nested
  abstract val checker: Property<XmlFormatChecker>

  /** Violations in [reportFile] are reported relative to this directory. */
  @get:Internal
  abstract val rootDirectory: DirectoryProperty

  @get:OutputFile
  abstract val reportFile: RegularFileProperty

  @TaskAction
  fun check() {
    val root = rootDirectory.get().asFile
    val checker = checker.get()
    val violations = sources.files.sorted().flatMap { file ->
      checker.check(file.readText()).map { file to it }
    }

    val report = reportFile.get().asFile
    report.parentFile.mkdirs()
    report.writeText(
      violations.joinToString(separator = "") { (file, violation) ->
        "${file.relativeTo(root).invariantSeparatorsPath}:${violation.line}: ${violation.message}\n"
      },
    )

    if (violations.isNotEmpty()) {
      throw GradleException(
        "Found ${violations.size} XML formatting violation(s):\n" +
          violations.joinToString("\n") { (file, violation) ->
            "${file.fileUri()}:${violation.line}: ${violation.message}"
          } +
          "\nReport: ${report.fileUri()}",
      )
    }
  }
}

/** `file:///absolute/path`, which terminals and the IDE console show as a link (`File.toURI` gives `file:/`). */
private fun File.fileUri(): String = toPath().toUri().toString()
