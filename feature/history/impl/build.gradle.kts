plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.msh91.arcyto.history"
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.data.local)
    implementation(projects.core.data.remote)
    implementation(projects.core.formatter)
    implementation(projects.core.tooling.extension)

    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.remote.provider)
    implementation(libs.navigation.compose)

    api(projects.feature.history.api)

    implementation(projects.feature.details.api)

    testImplementation(projects.core.tooling.test)
}