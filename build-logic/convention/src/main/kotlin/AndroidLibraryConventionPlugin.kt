import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import io.github.msh91.arcyto.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                project.disableBuildType("debug")
                buildTypes {
                    getByName("debug") {
                        matchingFallbacks += "release"
                    }
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}

private fun Project.disableBuildType(buildType: String) {
    val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
    androidComponents.apply {
        beforeVariants(selector().withBuildType(buildType)) { variant ->
            variant.enable = false
        }
    }
}