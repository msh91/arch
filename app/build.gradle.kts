plugins {
    id("convention.android")
    alias(libs.plugins.safeargs)
}

android {
    namespace = "io.github.msh91.arch"

    defaultConfig {
        applicationId = "io.github.msh91.arch"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
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
    implementation(libs.fragment.ktx)

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
    testImplementation(libs.arch.core.testing)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.esperesso)
}

