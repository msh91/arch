package io.github.msh91.arch.di.builder

import dagger.Binds
import dagger.Module
import io.github.msh91.arch.data.repository.user.TestUserRepository
import io.github.msh91.arch.data.repository.user.TestUserRepositoryImpl

/**
 * Every Repository is an interface and has different implementations.
 * With this module we can bind our desired implementation to be injected into UseCases or ViewModels.
 * This module will be installed in [ViewModelBuilder]
 */
@Module
interface RepositoryBuilder {
    @Binds
    fun userRepository(userRepositoryImpl: TestUserRepositoryImpl): TestUserRepository
}