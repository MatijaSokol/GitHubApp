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

    task("incrementMajor") {
      doLast {
        with(version) {
          versioning.setVersion(major + 1, 0, 0, 1)
        }
      }
    }

    task("incrementMinor") {
      doLast {
        with(version) {
          versioning.setVersion(major, minor + 1, 0, 1)
        }
      }
    }

    task("incrementPatch") {
      doLast {
        with(version) {
          versioning.setVersion(major, minor, patch + 1, 1)
        }
      }
    }

    task("incrementBuild") {
      doLast {
        with(version) {
          versioning.setVersion(major, minor, patch, build + 1)
        }
      }
    }

    task("printVersion") {
      doLast {
        with(version) {
          println(versionName)
          println(versionCode)
        }
      }
    }
  }
}