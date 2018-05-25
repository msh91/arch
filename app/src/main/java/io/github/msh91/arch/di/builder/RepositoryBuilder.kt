package io.github.msh91.arch.di.builder

import dagger.Binds
import dagger.Module
import io.github.msh91.arch.data.repository.user.TestUserRepository
import io.github.msh91.arch.data.repository.user.TestUserRepositoryImpl

@Module
interface RepositoryBuilder {
    @Binds
    fun userRepository(userRepositoryImpl: TestUserRepositoryImpl): TestUserRepository
}