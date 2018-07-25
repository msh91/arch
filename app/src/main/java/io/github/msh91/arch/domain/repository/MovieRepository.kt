package io.github.msh91.arch.domain.repository

import io.github.msh91.arch.domain.model.movie.Movie
import io.reactivex.Flowable

interface MovieRepository {
    /**
     * Attempts to get list of available movies from Database and if nothing exists try to get them
     * from cloud source and save them to local database
     *
     * @return a [Flowable] instance of movie list
     */
    fun getAllMovies(): Flowable<List<Movie>>
}