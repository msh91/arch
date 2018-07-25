package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.data.restful.APIs
import io.github.msh91.arch.data.restful.APIsWithToken

/**
 * The main implementation of [BaseCloudRepository] that call api services directly
 * @param apIs instance of without-token apis
 * @param apIsWithToken instance of with-token apis
 */
class CloudRepository(private val apIs: APIs, private val apIsWithToken: APIsWithToken) : BaseCloudRepository {

}
