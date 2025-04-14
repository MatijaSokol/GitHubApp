import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

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
      detektPlugins(libs.detekt.formatting)
      detektPlugins(libs.detekt.formatting.compose)
    }
  }

  private fun Project.configureKtlint() {
    val ktlintConfiguration = configurations.create("ktlint")

    val inputFiles = fileTree("src") { include("src/**/*.kt") }
    val outputDir = layout.buildDirectory.dir("reports")

    tasks.register<JavaExec>("ktlintCheck") {
        inputs.files(inputFiles)
        outputs.dir(outputDir)

        group = "Verification"
        description = "Check Kotlin code style."
        mainClass.set("com.pinterest.ktlint.Main")
        classpath = ktlintConfiguration
        args = listOf(
            "src/**/*.kt",
            "--reporter=plain",
            "--reporter=html,output=${outputDir.get().asFile.absolutePath}/ktlint.html",
        )
    }

    tasks.register<JavaExec>("ktlintFormat") {
      inputs.files(inputFiles)
      outputs.dir(outputDir)

      group = "Formatting"
      description = "Fix Kotlin code style deviations."
      mainClass.set("com.pinterest.ktlint.Main")
      classpath = ktlintConfiguration
      jvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
      args = listOf("-F", "src/**/*.kt")
    }

    dependencies {
      ktlintConfiguration(libs.ktlint.cli) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
      }
    }
  }
}

