package io.github.msh91.arch.di.builder

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Custom [MapKey] that accepts a [KClass] that extends [ViewModel] and use it as the key of ViewModels.
 *
 * @see io.github.msh91.arch.di.builder.ViewModelBuilder
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
