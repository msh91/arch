plugins {
    id("convention.android")
}

android {
    namespace = "io.github.msh91.arch.data"
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.coroutines.core)
    implementation(libs.arrow.core)
    implementation(libs.lifecycle.livedata)
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    implementation(libs.gson)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
}
