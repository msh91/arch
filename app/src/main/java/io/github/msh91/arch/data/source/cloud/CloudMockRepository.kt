package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Flowable

/**
 * Mock implementation of [BaseCloudRepository].
 *
 * <b>CAUTION : this implementation should be used only in debug mode</b>
 */
class CloudMockRepository : BaseCloudRepository {

    override fun requestUser(testUserModel: TestUserModel): Flowable<TestUserModel> {
        return Flowable.just(testUserModel)
    }
}