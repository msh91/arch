package io.github.msh91.arch.data.di.qualifier

import javax.inject.Qualifier

/**
 * A qualifier to identify without-token api services
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WithoutToken
