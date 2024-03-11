plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.safeargs) apply false
    alias(libs.plugins.ktlint) apply true
    alias(libs.plugins.kotlin.jvm) apply false
}
