package io.github.msh91.arch.di.qualifier.network

import javax.inject.Qualifier

/**
 * A qualifier to identify real cloud api calls
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Cloud