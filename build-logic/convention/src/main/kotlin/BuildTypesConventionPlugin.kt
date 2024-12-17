import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class BuildTypesConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      extensions.configure<ApplicationExtension> {
        configureSigningConfigs(this)
        configureBuildTypes(this)
      }
    }
  }

  private fun Project.configureSigningConfigs(
    applicationExtension: ApplicationExtension,
  ) {
    applicationExtension.apply {
      signingConfigs {
        getByName("debug") {
          storeFile = project.rootProject.file("release/debug.keystore")
          keyAlias = "androiddebugkey"
          storePassword = "android"
          keyPassword = "android"
        }

        create("release") {
          storeFile = project.rootProject.file("release/release.keystore")
          keyAlias = "githubapp"
          storePassword = System.getenv("GITHUBAPP_STORE_PASSWORD")
          keyPassword = System.getenv("GITHUBAPP_KEY_PASSWORD")
        }
      }
    }
  }

  private fun Project.configureBuildTypes(
    applicationExtension: ApplicationExtension,
  ) {
    applicationExtension.apply {
      buildTypes {
        debug {
          isDebuggable = true
        }
        release {
          signingConfig = signingConfigs.getByName("release")
          isMinifyEnabled = false
          proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
          )
        }
      }
    }
  }
}

