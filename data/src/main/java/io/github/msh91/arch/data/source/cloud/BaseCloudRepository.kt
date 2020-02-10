package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.data.model.movie.Movie

/**
 * Every api service defined here as a contract and will be implemented for real lifecycle in
 * [CloudRepository] and for mock lifecycle in [CloudMockRepository]
 */
interface BaseCloudRepository {
    suspend fun getAllMovies(): List<Movie>
}