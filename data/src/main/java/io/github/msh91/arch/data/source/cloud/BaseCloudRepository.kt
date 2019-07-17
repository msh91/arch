package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.domain.model.movie.Movie
import io.reactivex.Flowable

/**
 * Every api service defined here as a contract and will be implemented for real lifecycle in
 * [CloudRepository] and for mock lifecycle in [CloudMockRepository]
 */
interface BaseCloudRepository {
    fun getAllMovies(): Flowable<List<Movie>>
}