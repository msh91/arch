plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.safeargs)
}

android {
    compileSdk = 34
    namespace = "io.github.msh91.arch"

    defaultConfig {
        applicationId = "io.github.msh91.arch"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }

    buildTypes {

        debug {
        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.kotlin.bom)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.arrow.core)

    // Project Submodules
    implementation(project(":data"))

    // Jetpack
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewModel)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.fragment)

    // Dagger
    implementation(libs.dagger.core)
    implementation(libs.dagger.android.support)
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.compiler)

    // Retrofit - OkHttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okHttp.interceptor)

    // Others
    implementation(libs.anyChart)

    // Test Dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.arch.core.testing)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.esperesso)
}

