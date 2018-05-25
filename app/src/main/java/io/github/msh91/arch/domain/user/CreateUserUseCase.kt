package io.github.msh91.arch.domain.user

import io.github.msh91.arch.data.model.user.TestUserModel
import io.github.msh91.arch.data.repository.user.TestUserRepository
import io.github.msh91.arch.domain.BaseUseCase
import io.github.msh91.arch.util.ErrorUtil
import io.reactivex.Single
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
        private val userRepository: TestUserRepository,
        errorUtil: ErrorUtil
): BaseUseCase<TestUserModel>(errorUtil) {

    private lateinit var userModel: TestUserModel

    fun setParameters(userModel: TestUserModel): CreateUserUseCase {
        this.userModel = userModel
        return this
    }

    override fun buildUseCaseObservable(): Single<TestUserModel> {
        return userRepository.requestUser(userModel)
    }
}