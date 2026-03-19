import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      applyPlugins()
      configureAndroidCompose()
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.android.application)
      apply(libs.plugins.kotlin.compose.compiler)
    }
  }
}
