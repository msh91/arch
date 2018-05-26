package io.github.msh91.arch.data.repository.user

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Flowable

interface TestUserRepository {
    fun requestUser(testUserModel: TestUserModel): Flowable<TestUserModel>
}