import com.android.build.api.dsl.Lint
import org.gradle.api.Project

/**
 * Shared Android Lint configuration for application and library modules.
 *
 * Each module keeps its known, pre-existing issues in its own `lint-baseline.xml`, so `./gradlew lint`
 * fails only on new problems. Regenerate the baselines with `./gradlew updateLintBaseline`.
 */
internal fun Project.configureLint(lint: Lint) {
  lint.apply {
    baseline = file("lint-baseline.xml")
    abortOnError = true
  }
}
