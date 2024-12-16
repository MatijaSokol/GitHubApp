plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val appId = "com.matijasokol.githubapp"

android {
    namespace = "com.matijasokol.githubapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appId
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.matijasokol.githubapp.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    flavorDimensions += listOf("mode")

    productFlavors {
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

    sourceSets.getByName("free") {
        java {
            srcDirs("src/free/java")
        }
    }

    sourceSets.getByName("paid") {
        java {
            srcDirs("src/paid/java")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.repo.repoDatasource)
    implementation(projects.repo.repoDomain)
    implementation(projects.repo.uiRepoList)
    implementation(projects.repo.uiRepoDetail)

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.viewmodel)

    implementation(libs.coil.compose)

    implementation(libs.activity.compose)
    implementation(libs.compose.hilt.navigation)
    implementation(libs.compose.navigation)
    implementation(libs.material)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.sqldelight.driver.android)

    implementation(platform(libs.ktor.bom))
    implementation(libs.bundles.ktor)

    androidTestImplementation(projects.repo.repoDatasourceTest)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.compose.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.junit)
}