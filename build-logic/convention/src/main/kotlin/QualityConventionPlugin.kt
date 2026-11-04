import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import quality.KtlintSourceArguments
import quality.QualityExtension
import quality.XmlFormatCheckTask
import quality.XmlFormatChecker

class QualityConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      val quality = extensions.create<QualityExtension>("githubappQuality").apply {
        kotlinSources.from(kotlinSources())
      }
      configureDetekt(quality)
      configureKtlint(quality)
      configureAndroidLint()
      configureXmlFormatCheck()
    }
  }

  private fun Project.configureDetekt(quality: QualityExtension) {
    pluginManager.apply(libs.plugins.detekt)

    extensions.configure<DetektExtension> {
      config.setFrom(files("$rootDir/quality/detekt.yml"))
      // Detekt defaults to src/main and src/test only; scan every source set (flavors, androidTest, testFixtures, ...).
      source.setFrom(quality.kotlinSources)
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

  private fun Project.configureKtlint(quality: QualityExtension) {
    val ktlintConfiguration = configurations.create("ktlint")

    val reportFile = layout.buildDirectory.file("reports/ktlint.html")

    tasks.register<JavaExec>("ktlintCheck") {
        inputs.files(quality.kotlinSources).skipWhenEmpty()
        inputs.file(rootProject.file(".editorconfig"))
        outputs.file(reportFile)

        group = "Verification"
        description = "Check Kotlin code style."
        mainClass.set("com.pinterest.ktlint.Main")
        classpath = ktlintConfiguration
        args = listOf("--reporter=plain", "--reporter=html,output=${reportFile.get().asFile.absolutePath}")
        argumentProviders.add(KtlintSourceArguments(quality.kotlinSources))
    }

    // No outputs declared on purpose: ktlintFormat rewrites its own inputs, so it should run every time it's invoked.
    tasks.register<JavaExec>("ktlintFormat") {
      inputs.files(quality.kotlinSources).skipWhenEmpty()

      group = "Formatting"
      description = "Fix Kotlin code style deviations."
      mainClass.set("com.pinterest.ktlint.Main")
      classpath = ktlintConfiguration
      jvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
      args = listOf("-F")
      argumentProviders.add(KtlintSourceArguments(quality.kotlinSources))
    }

    dependencies {
      ktlintConfiguration(libs.ktlint.cli) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
      }
    }
  }

  private fun Project.configureAndroidLint() {
    plugins.withType<AppPlugin> {
      extensions.configure<ApplicationExtension> { lint { configureLint(this) } }
    }
    plugins.withType<LibraryPlugin> {
      extensions.configure<LibraryExtension> { lint { configureLint(this) } }
    }
  }

  private fun Project.configureXmlFormatCheck() {
    val xmlFormatCheck = tasks.register<XmlFormatCheckTask>("xmlFormatCheck") {
      group = "Verification"
      description = "Check XML indentation (multiple of 4, tab = 4), trailing whitespace and final newline."
      sources.from(fileTree("src") { include("**/*.xml") })
      checker.convention(XmlFormatChecker())
      rootDirectory.set(rootDir)
      reportFile.set(layout.buildDirectory.file("reports/xml-format.txt"))
    }
    plugins.withType<LifecycleBasePlugin> {
      tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) { dependsOn(xmlFormatCheck) }
    }
  }

  /** Every Kotlin file and script in every source set of the module. `include` patterns are relative to `src`. */
  private fun Project.kotlinSources(): ConfigurableFileTree = fileTree("src") { include(KOTLIN_GLOBS) }
}

/**
 * Kotlin files and scripts in every source set, relative to a module's `src` directory. `*.kts` keeps the scope of
 * Detekt's and ktlint's defaults, which also cover scripts.
 */
private val KOTLIN_GLOBS = listOf("**/*.kt", "**/*.kts")
