plugins {
    id("arcyto.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.msh91.arcyto.history.api"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}