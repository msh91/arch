package io.github.msh91.arch.data.repository.user

import io.github.msh91.arch.data.model.user.TestUserModel
import io.github.msh91.arch.data.repository.BaseCloudRepository
import io.github.msh91.arch.data.repository.CacheDelegate
import io.github.msh91.arch.di.qualifier.network.Mock
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestUserRepositoryFactory @Inject constructor(
        @Mock private val cloudRepository: BaseCloudRepository
) : TestUserRepository {

    private var cachedUserModel: TestUserModel? by CacheDelegate(CacheDelegate.CACHE_FOR_5_MINUTES)

    override fun requestUser(testUserModel: TestUserModel): Flowable<TestUserModel> {
        return cachedUserModel?.let { Flowable.just(it) }
                ?: cloudRepository
                        .requestUser(testUserModel)
                        .map {
                            cachedUserModel = it
                            it
                        }
    }
}