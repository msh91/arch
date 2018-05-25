package io.github.msh91.arch.data.repository

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Single

class CloudMockRepository : BaseCloudRepository {

    override fun requestUser(testUserModel: TestUserModel): Single<TestUserModel> {
        return Single.just(testUserModel)
    }
}