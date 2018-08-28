package io.github.msh91.arch.domain.usecase.base

/**
 * Should be used as parent class of all UseCases.
 */
abstract class UseCase<T> {

    abstract fun execute(): T

}