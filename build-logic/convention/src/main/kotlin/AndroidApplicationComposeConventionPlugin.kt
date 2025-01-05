import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply(libs.plugins.android.application)
      extensions.configure<ApplicationExtension>(::configureAndroidCompose)
    }
  }
}

