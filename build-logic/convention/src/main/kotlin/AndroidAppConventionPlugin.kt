import com.android.build.api.dsl.ApplicationExtension
import com.squareup.anvil.plugin.AnvilExtension
import io.github.msh91.arcyto.buildlogic.AppConfig
import io.github.msh91.arcyto.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.squareup.anvil")
            }
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig {
                    minSdk = AppConfig.MIN_SDK_VERSION
                    targetSdk = AppConfig.TARGET_SDK_VERSION
                    applicationId = AppConfig.APPLICATION_ID
                    versionCode = AppConfig.VERSION_CODE
                    versionName = AppConfig.VERSION_NAME
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables.useSupportLibrary = true
                    multiDexEnabled = true

                    buildTypes {
                        getByName("release") {
                            isMinifyEnabled = true
                            proguardFiles(
                                getDefaultProguardFile("proguard-android.txt"),
                                "proguard-rules.pro"
                            )
                        }
                        getByName("debug") {
                            isMinifyEnabled = false
                            matchingFallbacks += "release"
                        }
                    }
                }
                buildFeatures.compose = true
                buildFeatures.buildConfig = true
            }
            extensions.configure<AnvilExtension> {
                generateDaggerFactories.set(true)
            }
        }
    }
}