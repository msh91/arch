package io.github.msh91.arch

object Libs {
    object Plugins {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}"
        const val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"
    }

    object Modules {
        const val data = ":data"
    }

    object Jetpack {
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.arch}"
        const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.arch}"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.arch}"
        const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.arch}"
        const val room = "androidx.room:room-runtime:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val annotations = "androidx.annotation:annotation:${Versions.material}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    }

    object Common {
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        const val arrowCore = "io.arrow-kt:arrow-core:${Versions.arrow}"
        const val arrowSyntax = "io.arrow-kt:arrow-syntax:${Versions.arrow}"
        const val arrowMeta = "io.arrow-kt:arrow-meta:${Versions.arrow}"
        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        const val daggerAndroid = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        const val daggerAndroidCompiler = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
        const val gson = "com.google.code.gson:gson:${Versions.gson}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
        const val okHttpInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
        const val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"
        const val stetho_OkHttp = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Testing {
        const val junit = "junit:junit:${Versions.junit}"
        const val testRunner = "androidx.test:runner:${Versions.testRunner}"
        const val esperesso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val mockitoKotlin = "com.nhaarman:mockito-kotlin-kt1.1:${Versions.mockitoKotlin}"
        const val archTesting = "androidx.arch.core:core-testing:${Versions.archTest}"
    }
}
