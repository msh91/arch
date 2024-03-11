package io.github.msh91.plugin

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal object Catalog {

    private val Project.internalLibs: Lazy<VersionCatalog>
        get() = lazy { extensions.getByType<VersionCatalogsExtension>().named("libs") }

    val Project.libs: VersionCatalog
        get() = internalLibs.value
}
