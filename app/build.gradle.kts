plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.transdoor.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.transdoor.app"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation (libs.squareup.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation ("androidx.recyclerview:recyclerview:1.4.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation(libs.logging.interceptor)
    implementation (libs.squareup.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.androidx.recyclerview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)








}
