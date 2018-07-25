package io.github.msh91.arch.data.repository.movie

import io.github.msh91.arch.data.source.cloud.BaseCloudRepository
import io.github.msh91.arch.data.source.db.dao.MovieDao
import io.github.msh91.arch.di.qualifier.network.Mock
import io.github.msh91.arch.domain.model.movie.Movie
import io.github.msh91.arch.domain.repository.MovieRepository
import io.reactivex.Flowable
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
        @Mock private val cloudRepository: BaseCloudRepository,
        private val movieDao: MovieDao
) : MovieRepository {

    override fun getAllMovies(): Flowable<List<Movie>> {
        return Flowable.fromCallable { movieDao.getCount() }
                .flatMap {
                    if (it == 0)
                        getMoviesFromCloud()
                    else
                        movieDao.getAllMovies()
                }
    }

    private fun getMoviesFromCloud(): Flowable<List<Movie>> {
        return cloudRepository
                .getAllMovies()
                .map(this::insertMoviesToDb)
    }

    private fun insertMoviesToDb(movies: List<Movie>): List<Movie> {
        movieDao.insertMovies(movies)
        return movies
    }
}