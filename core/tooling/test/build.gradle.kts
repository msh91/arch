plugins {
    id("arcyto.android.library")
}

android {
    namespace = "io.github.msh91.arcyto.core.tooling.test"
}

dependencies {
    api(libs.androidx.test.core)
    api(libs.androidx.test.ext)
    api(libs.androidx.test.rules)
    api(libs.coroutines.test)
    api(libs.junit)
    api(libs.kotlin.test)
    api(libs.mockk)
    api(libs.turbine)
}