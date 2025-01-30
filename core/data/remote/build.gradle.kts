import java.util.Properties
import kotlin.apply

plugins {
    id("arcyto.android.library")
    id("arcyto.anvil.library")
}
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}
val apiKey = localProperties.getProperty("API_KEY") ?: "API KEY Not Found!" // Default if not found

android {
    buildFeatures.buildConfig = true
    namespace = "io.github.msh91.arcyto.core.data.remote"
    defaultConfig {
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }
}

dependencies {
    api(libs.bundles.remote.builder)
    implementation(libs.bundles.coroutines)
    implementation(projects.core.data.local)
    implementation(projects.core.di)
    testImplementation(projects.core.tooling.test)
}