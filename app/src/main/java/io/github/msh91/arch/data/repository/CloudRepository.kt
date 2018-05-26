package io.github.msh91.arch.data.repository

import io.github.msh91.arch.data.model.user.TestUserModel
import io.github.msh91.arch.data.restful.APIs
import io.github.msh91.arch.data.restful.APIsWithToken
import io.reactivex.Flowable

class CloudRepository(private val apIs: APIs, private val apIsWithToken: APIsWithToken) : BaseCloudRepository {

    override fun requestUser(createUserModel: TestUserModel): Flowable<TestUserModel> {
        return apIs.requestUser(createUserModel)
    }
}
