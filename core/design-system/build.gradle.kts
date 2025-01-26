plugins {
    id("arcyto.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "io.github.msh91.arcyto.core.design"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.activity.compose)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel)
    api(libs.androidx.material3)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling)
    api(libs.androidx.ui.tooling.preview)
}