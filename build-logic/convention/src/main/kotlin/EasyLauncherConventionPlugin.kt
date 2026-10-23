import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.project.starter.easylauncher.filter.ChromeLikeFilter
import com.project.starter.easylauncher.plugin.EasyLauncherExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

/**
 * Badges the launcher icon of debug builds with a bottom ribbon that names the installed variant
 * (e.g. `devFree`, `prodPaid`), so it is clear on the home screen which variant is currently installed.
 */
class EasyLauncherConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply(libs.plugins.easylauncher)

      val easyLauncher = extensions.getByType<EasyLauncherExtension>()
      easyLauncher.buildTypes.register(RELEASE_BUILD_TYPE) {
        enable(false)
      }

      extensions.configure<ApplicationAndroidComponentsExtension> {
        // Easylauncher reads its `variants` container in its own onVariants callback, which runs after
        // beforeVariants, so registering the per-variant ribbon here is early enough.
        beforeVariants(selector().withBuildType(DEBUG_BUILD_TYPE)) { variantBuilder ->
          val label = ribbonLabel(flavorName = variantBuilder.flavorName, variantName = variantBuilder.name)
          easyLauncher.variants.register(variantBuilder.name) {
            filters(debugRibbonFilter(label))
          }
        }
      }
    }
  }

  private fun ribbonLabel(flavorName: String?, variantName: String): String =
    flavorName?.takeIf { it.isNotEmpty() } ?: variantName

  private fun debugRibbonFilter(label: String): ChromeLikeFilter = ChromeLikeFilter(
    label = label,
    ribbonColor = RIBBON_COLOR,
    labelColor = LABEL_COLOR,
    overlayHeight = OVERLAY_HEIGHT,
    gravity = ChromeLikeFilter.Gravity.BOTTOM,
    textSizeRatio = TEXT_SIZE_RATIO,
  )
}

private const val DEBUG_BUILD_TYPE = "debug"
private const val RELEASE_BUILD_TYPE = "release"

private const val RIBBON_COLOR = "#CCB71C1C"
private const val LABEL_COLOR = "#FFFFFF"

/** Ribbon height as a fraction of the visible icon height. */
private const val OVERLAY_HEIGHT = 0.25F

/** Label font size as a fraction of the visible icon height. */
private const val TEXT_SIZE_RATIO = 0.13F
