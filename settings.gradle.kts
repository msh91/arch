import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://dl.bintray.com/arrow-kt/arrow-kt/")
    }
}

//include(":app", ":data")
val excludedModules = listOf("buildSrc")
File(rootDir.path)
    .listFiles(java.io.FileFilter { it.isDirectory })
    ?.mapNotNull { module -> File(module, "build.gradle.kts") }
    ?.filter { it.exists() && !excludedModules.contains(it.parentFile.name) }
    ?.onEach {
        val isBuildModule = File(it.parentFile, "settings.gradle.kts").exists()
        val moduleName = it.parentFile.name
        if (isBuildModule) {
            includeBuild(moduleName)
        } else {
            include(":${moduleName}")
        }
    }
