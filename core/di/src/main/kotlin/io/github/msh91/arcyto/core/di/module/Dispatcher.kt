package io.github.msh91.arcyto.core.di.module

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val type: DispatcherType)

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

enum class DispatcherType {
    IO,
    Default,
    Main,
}
