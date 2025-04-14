import app.cash.sqldelight.gradle.SqlDelightExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class SQLDelightConventionPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    with(project) {
      pluginManager.apply(libs.plugins.sqldelight)

      extensions.configure<SqlDelightExtension> {
        // this will be the name of the generated database class
        databases.create("RepoDatabase") {
          // package name used for the database class
          packageName.set("com.matijasokol.repo.datasource.cache")

          // generate suspending query methods with asynchronous drivers
          generateAsync.set(true)

          // directory where .db schema files should be stored, relative to the project root
          // use ./gradlew data:tasks to list all available tasks for generating schema
          // available task should be run before every migration
          schemaOutputDirectory.set(file("src/main/sqldelight/databases"))

          // migration files will fail during the build process if there are any errors in them
          verifyMigrations.set(true)
        }
      }

      dependencies {
        implementation(libs.sqldelight.coroutines)
        // enable driver separately in app module - implementation(libs.sqldelight.android)
      }
    }
  }
}
