import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

class JvmLibraryConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      applyPlugins()

      configureKotlinJvm()

      tasks.withType<Test> {
        useJUnitPlatform()
      }
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.kotlin.jvm)
      apply(libs.plugins.githubapp.quality)
    }
  }
}