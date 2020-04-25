package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.data.model.movie.Movie

/**
 * This interface can be used as Retrofit api service.
 *
 * For test purposes use
 */
interface MovieDataSource {
    suspend fun getAllMovies(): List<Movie>
}