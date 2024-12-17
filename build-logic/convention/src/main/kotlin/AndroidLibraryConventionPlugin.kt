import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import versioning.Versioning

class AndroidLibraryConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      val version = Versioning(project.rootDir.path).readVersion()

      applyPlugins()

      extensions.configure<LibraryExtension> {
        configureAndroid(version, this)
        configureKotlinAndroid(this)
      }
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.android.library)
      apply(libs.plugins.kotlin.android)
      apply(libs.plugins.githubapp.quality)
    }
  }

  private fun Project.configureAndroid(
    version: Versioning.Version,
    libraryExtension: LibraryExtension,
  ) = libraryExtension.apply {
    buildFeatures {
      buildConfig = true
    }

    defaultConfig.apply {
      targetSdk = libs.versions.targetSdk.get().toInt()

      versionCode = version.versionCode
      versionName = version.versionName

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }
}