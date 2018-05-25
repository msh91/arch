package io.github.msh91.arch.data.repository

import io.github.msh91.arch.data.model.user.TestUserModel
import io.reactivex.Single

interface BaseCloudRepository {
    fun requestUser(createUserModel: TestUserModel): Single<TestUserModel>
}