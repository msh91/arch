plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.msh91.arcyto.details.impl"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.data.local)
    implementation(projects.core.data.remote)
    implementation(projects.core.tooling.extension)

    api(projects.feature.details.api)

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.remote.provider)
    implementation(libs.glide.compose)
    implementation(libs.navigation.compose)

    testImplementation(projects.core.tooling.test)
}