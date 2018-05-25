package io.github.msh91.arch.data.repository.user

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Single
import javax.inject.Inject

class TestUserRepositoryImpl
@Inject constructor(private val testUserRepositoryFactory: TestUserRepositoryFactory) : TestUserRepository {

    override fun requestUser(testUserModel: TestUserModel): Single<TestUserModel> {
        return testUserRepositoryFactory.requestUser(testUserModel)
    }
}