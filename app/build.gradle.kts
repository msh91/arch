plugins {
    id("arcyto.android.app")
    kotlin("kapt")
}

android {
    namespace = "io.github.msh91.arcyto"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)

    kapt(libs.dagger.compiler)

    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.data.local)
    implementation(projects.core.data.remote)
    implementation(projects.core.tooling.extension)
    implementation(projects.feature.details.impl)
    implementation(projects.feature.history)

    testImplementation(libs.junit)
    testImplementation(projects.core.tooling.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
