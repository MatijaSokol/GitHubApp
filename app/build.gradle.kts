plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

    alias(libs.plugins.githubapp.application)
    alias(libs.plugins.githubapp.application.compose)
}

val appId = "com.matijasokol.githubapp"

android {
    namespace = "com.matijasokol.githubapp"

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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreUi)
    implementation(projects.repo.datasource)
    implementation(projects.repo.domain)
    implementation(projects.repo.list)
    implementation(projects.repo.detail)

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.viewmodel)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)

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
