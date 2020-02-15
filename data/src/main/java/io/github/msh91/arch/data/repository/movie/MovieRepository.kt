package io.github.msh91.arch.data.repository.movie

import io.github.msh91.arch.data.di.qualifier.network.Mock
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.repository.BaseRepository
import io.github.msh91.arch.data.source.cloud.BaseCloudRepository
import io.github.msh91.arch.data.source.db.dao.MovieDao
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MovieRepository @Inject constructor(
        @Mock private val cloudRepository: BaseCloudRepository,
        private val movieDao: MovieDao
) : BaseRepository(Dispatchers.IO) {

    suspend fun getAllMovies(): List<Movie> {
        // at first step we will check if any movie exists on db or not
        return if (movieDao.getCount() == 0) {
            // nothing exists on database, so get them from cloud
            safeCall(this::getMoviesFromCloud)
            // there are some movies in db, so attempt to get them
        } else {
            // nothing exists on database, so get them from cloud
            // there are some movies in db, so attempt to get them
            safeCall(movieDao::getAllMovies)
        }
    }

    private suspend fun getMoviesFromCloud(): List<Movie> {
        val movies = cloudRepository.getAllMovies()
        insertMoviesToDb(movies)
        return movies
    }

    /**
     * Attempts to insert fetched movies to database and returns the given list
     */
    private suspend fun insertMoviesToDb(movies: List<Movie>): List<Movie> {
        movieDao.insertMovies(movies)
        return movies
    }
}