import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val javaVersion = JavaVersion.VERSION_21
private val jvmTargetValue = JvmTarget.JVM_21

/**
 * Configure base Kotlin with Android options for both application and library modules
 * Breaking change after migration to AGP 9, since CommonExtension is no longer available, so we need to check for both extensions explicitly
 * Depending on the presence of ApplicationExtension or LibraryExtension, it will configure the appropriate options
 */
internal fun Project.configureKotlinAndroid() {
  val applicationExtension = runCatching { project.extensions.getByType<ApplicationExtension>() }.getOrNull()
  val libraryExtension = runCatching { project.extensions.getByType<LibraryExtension>() }.getOrNull()

  when {
    applicationExtension != null -> configureKotlinAndroid(applicationExtension)
    libraryExtension != null -> configureKotlinAndroid(libraryExtension)
    else -> error("Neither ApplicationExtension nor LibraryExtension found on project ${project.name}")
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  configureKotlin()
}

/**
 * Configure base Kotlin with Android options for application modules
 */
private fun Project.configureKotlinAndroid(applicationExtension: ApplicationExtension) {
  applicationExtension.apply {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
      minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
    }
  }
}

/**
 * Configure base Kotlin with Android options for library modules
 */
private fun Project.configureKotlinAndroid(libraryExtension: LibraryExtension) {
  libraryExtension.apply {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
      minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
    }
  }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
  extensions.configure<JavaPluginExtension> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
  }

  tasks.withType<Test> {
      useJUnitPlatform()
  }

  configureKotlin()
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(jvmTargetValue)
      freeCompilerArgs.addAll(listOf(
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
        "-Xreturn-value-checker=full",
      ))
    }
  }
}
