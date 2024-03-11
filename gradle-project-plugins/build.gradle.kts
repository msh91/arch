plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
    alias(libs.plugins.kotlin.kapt)
}

gradlePlugin {
    plugins {
        register("android-convention") {
            id = "convention.android"
            implementationClass = "io.github.msh91.plugin.AndroidConvention"
        }
    }
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradle)
    compileOnly(gradleKotlinDsl())
    compileOnly(gradleApi())
    compileOnly(localGroovy())
}
