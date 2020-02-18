package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.restful.APIs
import io.github.msh91.arch.data.restful.APIsWithToken

/**
 * The main implementation of [BaseCloudRepository] that call api services directly
 * @param apIs instance of without-token apis
 * @param apIsWithToken instance of with-token apis
 */
class CloudRepository(private val apIs: APIs, private val apIsWithToken: APIsWithToken) : BaseCloudRepository {
    override suspend fun getAllMovies(): List<Movie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
