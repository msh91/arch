plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "io.github.msh91.arcyto.core.di"
}

dependencies {
    api(libs.dagger)
    implementation(libs.androidx.lifecycle.viewmodel)
    testImplementation(libs.junit)
}