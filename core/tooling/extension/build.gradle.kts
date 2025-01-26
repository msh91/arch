plugins {
    id("arcyto.android.library")
}

android {
    namespace = "io.github.msh91.arcyto.core.tooling.extension"
}

dependencies {
    api(libs.bundles.coroutines)
}