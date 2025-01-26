plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
}

android {
    namespace = "io.github.msh91.arcyto.core.data.remote"
}

dependencies {
    api(libs.bundles.remote.builder)
    implementation(libs.bundles.coroutines)
    implementation(projects.core.di)
    testImplementation(libs.junit)
}