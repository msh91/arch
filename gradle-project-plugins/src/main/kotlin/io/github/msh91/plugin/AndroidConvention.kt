package io.github.msh91.plugin

import com.android.build.gradle.BaseExtension
import io.github.msh91.plugin.Catalog.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidConvention : Plugin<Project> {
    private val appModules = listOf("app")

    override fun apply(target: Project) {
        with(target) {
            configurePlugins(target.name)
            configureAndroid()
            configureKotlin()
            configureDependencies()
        }
    }

    private fun Project.configureAndroid() {
        configure<BaseExtension> {
            compileSdkVersion(35)

            defaultConfig {
                it.minSdk = 23
                it.targetSdk = 35
                it.versionCode = 1
                it.versionName = "1.0"
                it.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                it.vectorDrawables.useSupportLibrary = true
                it.multiDexEnabled = true
            }

            buildTypes {

                it.getByName("release") { buildType ->
                    buildType.isMinifyEnabled = true
                    buildType.proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                }

                it.getByName("debug") { buildType ->
                    buildType.isMinifyEnabled = false
                }
            }

            compileOptions {
                it.sourceCompatibility = JavaVersion.VERSION_17
                it.targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }

    private fun Project.configureKotlin() {
        tasks.withType(KotlinCompile::class.java) {
            it.compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    private fun Project.configurePlugins(moduleName: String) {
        if (appModules.contains(moduleName)) {
            plugins.apply(libs.findPlugin("android-application").get().get().pluginId)
        } else {
            plugins.apply(libs.findPlugin("android-library").get().get().pluginId)
        }
        plugins.apply(libs.findPlugin("kotlin-android").get().get().pluginId)
        plugins.apply(libs.findPlugin("kotlin-kapt").get().get().pluginId)
    }

    private fun Project.configureDependencies() {
        dependencies {
            add("implementation", libs.findLibrary("kotlin-stdlib").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("mockk").get())
        }
    }
}
