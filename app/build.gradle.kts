plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.matijasokol.githubapp"
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.appId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.kotlinCompilerExtensionVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(Modules.repoDataSource))
    implementation(project(Modules.repoDomain))
    implementation(project(Modules.ui_repoList))
    implementation(project(Modules.ui_repoDetail))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.lifecycleVmKtx)

    implementation(Coil.coil)

    implementation(Compose.activity)
    implementation(Compose.ui)
    implementation(Compose.material)
    implementation(Compose.tooling)
    implementation(Compose.navigation)
    implementation(Compose.hiltNavigation)

    implementation(Google.material)

    implementation(Hilt.android)
    kapt(Hilt.compiler)

    implementation(Ktor.core)
    implementation(Ktor.clientSerialization)
    implementation(Ktor.android)
    implementation(Ktor.contentNegotiation)
    implementation(Ktor.json)
    implementation(Ktor.logging)
}