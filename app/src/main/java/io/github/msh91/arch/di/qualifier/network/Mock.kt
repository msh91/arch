package io.github.msh91.arch.di.qualifier.network

import javax.inject.Qualifier

/**
 * A qualifier to identify mock cloud api calls
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Mock