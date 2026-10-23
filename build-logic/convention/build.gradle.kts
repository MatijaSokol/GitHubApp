import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.sqldelight.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.easylauncher.gradlePlugin)

    // used for type safe access to version catalog
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kluent)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
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
        register("easyLauncherPlugin") {
            id = "githubapp.easylauncher"
            implementationClass = "EasyLauncherConventionPlugin"
        }
    }
}