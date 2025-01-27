pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Arcyto"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Arcyto"
include(":app")
include(":core:design-system")
include(":core:di")
include(":core:data:local")
include(":core:data:remote")
include(":core:tooling:extension")
include(":core:tooling:test")
include(":feature:details")
include(":feature:history")
