package io.github.msh91.arch.data.repository.movie

import io.github.msh91.arch.data.di.qualifier.network.Mock
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.source.cloud.BaseCloudRepository
import io.github.msh91.arch.data.source.db.dao.MovieDao
import io.reactivex.Flowable
import javax.inject.Inject

class MovieRepository @Inject constructor(
        @Mock private val cloudRepository: BaseCloudRepository,
        private val movieDao: MovieDao
) {

    fun getAllMovies(): Flowable<List<Movie>> {
        // at first step we will check if any movie exists on db or not
        return Flowable.fromCallable { movieDao.getCount() }
                .flatMap {
                    if (it == 0)
                        // nothing exists on database, so get them from cloud
                        getMoviesFromCloud()
                    else
                        // there are some movies in db, so attempt to get them
                        movieDao.getAllMovies()
                }
    }

    private fun getMoviesFromCloud(): Flowable<List<Movie>> {
        return cloudRepository
                .getAllMovies()
                .map(this::insertMoviesToDb)
    }

    /**
     * Attempts to insert fetched movies to database and returns the given list
     */
    private fun insertMoviesToDb(movies: List<Movie>): List<Movie> {
        movieDao.insertMovies(movies)
        return movies
    }
}