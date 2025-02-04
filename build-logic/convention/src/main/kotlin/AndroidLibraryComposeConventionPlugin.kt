import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply(libs.plugins.android.library)
      pluginManager.apply(libs.plugins.kotlin.compose.compiler)
      extensions.configure<LibraryExtension>(::configureAndroidCompose)
    }
  }
}