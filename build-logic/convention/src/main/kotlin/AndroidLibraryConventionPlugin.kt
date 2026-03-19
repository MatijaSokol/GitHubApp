import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      applyPlugins()
      extensions.configure<LibraryExtension> { configureAndroid(this) }
      configureKotlinAndroid()
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.android.library)
      apply(libs.plugins.githubapp.quality)
      apply(libs.plugins.ksp)
      apply(libs.plugins.kotlinx.serialization)
    }
  }

  private fun Project.configureAndroid(libraryExtension: LibraryExtension) = libraryExtension.apply {
    buildFeatures {
      buildConfig = true
      resValues = true
    }

    defaultConfig {
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }
}