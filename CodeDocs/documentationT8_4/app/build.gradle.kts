plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "es.javiercarrasco.documentationt84"
    compileSdk = 36

    defaultConfig {
        applicationId = "es.javiercarrasco.documentationt84"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ViewModel para Compose
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.3")

    // Permisos (rememberPermissionState)
//    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // CameraX core
    implementation("androidx.camera:camera-core:1.5.0")

    // CameraX Lifecycle
    implementation("androidx.camera:camera-lifecycle:1.5.0")

    // CameraX View (para PreviewView)
    implementation("androidx.camera:camera-view:1.5.0")

    // CameraX Camera2 (backend recomendado)
    implementation("androidx.camera:camera-camera2:1.5.0")

    // Si se utiliza análisis (opcional)
    implementation("androidx.camera:camera-extensions:1.5.0")
}