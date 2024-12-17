import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

class QualityConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      configureDetekt()
      configureKtlint()
    }
  }

  private fun Project.configureDetekt() {
    pluginManager.apply(libs.plugins.detekt)

    extensions.configure<DetektExtension> {
      config.setFrom(files("$rootDir/quality/detekt.yml"))
    }

    tasks.withType(Detekt::class.java).configureEach {
      reports {
        html.required.set(true)
        txt.required.set(true)
        xml.required.set(true)
      }
    }

    dependencies {
      add("detektPlugins", libs.detekt.formatting)
      add("detektPlugins", libs.detekt.formatting.compose)
    }
  }

  private fun Project.configureKtlint() {
    pluginManager.apply(libs.plugins.ktlint)

    extensions.configure<KtlintExtension> {
      debug.set(true)
      version.set(libs.versions.ktlint)
      reporters {
        reporter(ReporterType.HTML)
        reporter(ReporterType.PLAIN)
      }
      filter {
        exclude { it.file.path.contains(layout.buildDirectory.dir("generated").get().toString()) }
        include { it.file.path.contains(layout.buildDirectory.dir("kotlin").get().toString()) }
      }
    }
  }
}

