import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      applyPlugins()
      configureAndroidCompose()
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.android.library)
      apply(libs.plugins.kotlin.compose.compiler)
    }
  }
}
