import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      applyPlugins()
      configureKotlinJvm()
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.kotlin.jvm)
      apply(libs.plugins.githubapp.quality)
      apply(libs.plugins.ksp)
      apply(libs.plugins.kotlinx.serialization)
    }
  }
}