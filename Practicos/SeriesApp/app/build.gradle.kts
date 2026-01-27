plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "edu.practico.myapplication"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "edu.practico.myapplication"
        minSdk = 30
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // Coil for Compose
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    // Conversor para JSON (Gson)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // Corutinas (para llamadas asíncronas)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // ROOM dependencies
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4") // Soporte para Coroutines y Kotlin Extensions.
    ksp("androidx.room:room-compiler:2.8.4") // KSP para procesamiento de anotaciones.

    // ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
}