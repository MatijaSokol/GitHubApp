import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.matijasokol.githubapp.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.sqldelight.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)

    // used for type safe access to version catalog
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("androidApplicationPlugin") {
            id = "githubapp.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationComposePlugin") {
            id = "githubapp.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryPlugin") {
            id = "githubapp.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryComposePlugin") {
            id = "githubapp.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("jvmLibraryPlugin") {
            id = "githubapp.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("versioningPlugin") {
            id = "githubapp.versioning"
            implementationClass = "versioning.VersioningPlugin"
        }
        register("productFlavorsPlugin") {
            id = "githubapp.productflavors"
            implementationClass = "ProductFlavorsConventionPlugin"
        }
        register("buildTypesPlugin") {
            id = "githubapp.buildtypes"
            implementationClass = "BuildTypesConventionPlugin"
        }
        register("sqldelightPlugin") {
            id = "githubapp.sqldelight"
            implementationClass = "SQLDelightConventionPlugin"
        }
        register("qualityPlugin") {
            id = "githubapp.quality"
            implementationClass = "QualityConventionPlugin"
        }
    }
}