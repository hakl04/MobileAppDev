plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.assignment3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.assignment3"
        minSdk = 33
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.firebase.database)
    implementation(libs.androidx.ui.android)
    implementation(libs.firebase.storage)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("io.ktor:ktor-client-core:2.2.0") // Core client
    implementation ("io.ktor:ktor-client-cio:2.2.0") // CIO engine
    implementation ("io.ktor:ktor-client-content-negotiation:2.2.0") // Content negotiation
    implementation ("io.ktor:ktor-serialization-kotlinx-json:2.2.0") // Kotlinx JSON
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0") // Kotlinx serialization
}