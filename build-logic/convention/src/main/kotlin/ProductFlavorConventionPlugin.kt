import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ProductFlavorsConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.extensions.configure<ApplicationExtension> {
      flavorDimensions += listOf("environment", "mode")

      productFlavors {
        create("dev") {
          dimension = "environment"
        }

        create("prod") {
          dimension = "environment"
        }

        create("free") {
          dimension = "mode"
          resValue("string", "app_name", "GitHub App Free")
          applicationIdSuffix = ".free"
        }
        create("paid") {
          dimension = "mode"
          resValue("string", "app_name", "GitHub App")
        }
      }
    }
  }
}