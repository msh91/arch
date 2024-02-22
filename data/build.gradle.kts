plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    compileSdk = 34
    namespace = "io.github.msh91.arch.data"

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(libs.kotlin.bom)
    implementation(libs.coroutines.core)
    implementation(libs.arrow.core)
    implementation(libs.lifecycle.livedata)
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    implementation(libs.gson)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
