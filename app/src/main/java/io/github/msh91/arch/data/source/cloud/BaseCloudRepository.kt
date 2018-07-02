package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Flowable

/**
 * Every api service defined here as a contract and will be implemented for real lifecycle in
 * [CloudRepository] and for mock lifecycle in [CloudMockRepository]
 */
interface BaseCloudRepository {
    /**
     * test api service to request creation of a user
     */
    fun requestUser(createUserModel: TestUserModel): Flowable<TestUserModel>
}