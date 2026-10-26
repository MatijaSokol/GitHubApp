import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.kotlin.dsl.configure
import versioning.Versioning

class AndroidApplicationConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      applyPlugins()
      extensions.configure<ApplicationExtension> { configureAndroid( this) }
      configureArchivesName()
      configureKotlinAndroid()
    }
  }

  private fun Project.applyPlugins() {
    with(pluginManager) {
      apply(libs.plugins.android.application)
      apply(libs.plugins.ksp)
      apply(libs.plugins.hilt)
      apply(libs.plugins.githubapp.productflavors)
      apply(libs.plugins.githubapp.buildtypes)
      apply(libs.plugins.githubapp.versioning)
      apply(libs.plugins.githubapp.quality)
      apply(libs.plugins.githubapp.easylauncher)
    }
  }

  /**
   * Output files are named `<AppName>-<versionName>-<flavors>-<buildType>`,
   * e.g. `GitHubApp-1.3.0.1-prod-paid-release.apk`, so local and CI builds share one convention.
   */
  private fun Project.configureArchivesName() {
    val versionName = Versioning(rootDir.path).readVersion().versionName
    extensions.configure<BasePluginExtension> {
      archivesName.set("${rootProject.name}-$versionName")
    }
  }

  private fun Project.configureAndroid(applicationExtension: ApplicationExtension) = applicationExtension.apply {
    buildFeatures {
      buildConfig = true
      resValues = true
    }

    defaultConfig {
      targetSdk = libs.versions.targetSdk.get().toInt()

      val version = Versioning(rootDir.path).readVersion()

      versionCode = version.versionCode
      versionName = version.versionName

      testInstrumentationRunner = "com.matijasokol.githubapp.CustomTestRunner"
    }
  }
}


