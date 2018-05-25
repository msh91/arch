package io.github.msh91.arch.data.repository

import io.github.msh91.arch.data.model.user.TestUserModel
import io.github.msh91.arch.data.restful.APIs
import io.github.msh91.arch.data.restful.APIsWithToken
import io.reactivex.Single

class CloudRepository(private val apIs: APIs, private val apIsWithToken: APIsWithToken) : BaseCloudRepository {

    override fun requestUser(createUserModel: TestUserModel): Single<TestUserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
