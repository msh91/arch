plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
}

android {
    namespace = "io.github.msh91.arcyto.core.formatter"
}

dependencies {
    implementation(projects.core.data.local)
    implementation(projects.core.di)
    implementation(projects.core.tooling.extension)
    testImplementation(projects.core.tooling.test)
}