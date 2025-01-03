import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {
    dependencies {
      val bom = libs.compose.bom
      add("implementation", platform(bom))
      add("implementation", libs.bundles.compose)
      add("debugImplementation", libs.bundles.compose.debug)
      add("androidTestImplementation", platform(bom))
    }
  }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      freeCompilerArgs.addAll(listOf(
        "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
        "-opt-in=androidx.compose.runtime.ExperimentalComposeApi",
        "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        "-opt-in=coil3.annotation.ExperimentalCoilApi",
      ))

      // generates compose metrics files
      // run with ./gradlew :[modulename]:assembleRelease -Pgithubapp.enableComposeCompilerReports=true
      if (findProperty("githubapp.enableComposeCompilerReports") == "true") {
        // ../[modulename]/build/compose_metrics
        val dir = "${layout.buildDirectory.get()}/compose_metrics"

        freeCompilerArgs.addAll(listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$dir",
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$dir",
        ))
      }
    }
  }
}