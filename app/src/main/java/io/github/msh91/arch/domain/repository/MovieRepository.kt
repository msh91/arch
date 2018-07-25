package io.github.msh91.arch.domain.repository

import io.github.msh91.arch.domain.model.movie.Movie
import io.reactivex.Flowable

interface MovieRepository {
    fun getAllMovies(): Flowable<List<Movie>>
}