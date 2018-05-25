package io.github.msh91.arch.data.repository.user

import io.github.msh91.arch.data.model.user.TestUserModel
import io.github.msh91.arch.data.repository.BaseCloudRepository
import io.github.msh91.arch.di.qualifier.network.Mock
import io.reactivex.Single

class TestUserRepositoryFactory(@Mock val cloudRepository: BaseCloudRepository): TestUserRepository {

    override fun requestUser(testUserModel: TestUserModel): Single<TestUserModel> {
        return cloudRepository.requestUser(testUserModel)
    }
}