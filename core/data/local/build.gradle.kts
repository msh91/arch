plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
}

android {
    namespace = "io.github.msh91.arcyto.core.data.local"
}

dependencies {
    api(libs.datastore.preferences)
    implementation(projects.core.di)
    testImplementation(libs.junit)
}