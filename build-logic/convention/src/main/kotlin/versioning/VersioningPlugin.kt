package versioning

import org.gradle.api.Plugin
import org.gradle.api.Project

class VersioningPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    val versioning = Versioning(project.rootDir.path)
    project.addTasks(versioning)
  }

  private fun Project.addTasks(versioning: Versioning) {
    val version = versioning.readVersion()

    tasks.register("incrementMajor") {
      doLast {
        with(version) {
          versioning.setVersion(major + 1, 0, 0, 1)
        }
      }
    }

    tasks.register("incrementMinor") {
      doLast {
        with(version) {
          versioning.setVersion(major, minor + 1, 0, 1)
        }
      }
    }

    tasks.register("incrementPatch") {
      doLast {
        with(version) {
          versioning.setVersion(major, minor, patch + 1, 1)
        }
      }
    }

    tasks.register("incrementBuild") {
      doLast {
        with(version) {
          versioning.setVersion(major, minor, patch, build + 1)
        }
      }
    }

    tasks.register("printVersion") {
      doLast {
        with(version) {
          println(versionName)
          println(versionCode)
        }
      }
    }

    tasks.register("printVersionName") {
      doLast {
        println(versioning.readVersion().versionName)
      }
    }
  }
}
