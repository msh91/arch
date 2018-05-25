package io.github.msh91.arch.data.repository.user

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Single

class TestUserRepositoryImpl : TestUserRepository {
    override fun requestUser(testUserModel: TestUserModel): Single<TestUserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}