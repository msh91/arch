package io.github.msh91.arch.data.repository.movie

import arrow.core.Either
import arrow.core.Either.Companion
import arrow.core.Either.Right
import io.github.msh91.arch.data.di.qualifier.network.Stub
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.model.response.Error
import io.github.msh91.arch.data.repository.BaseRepository
import io.github.msh91.arch.data.source.cloud.MovieDataSource
import io.github.msh91.arch.data.source.db.dao.MovieDao
import javax.inject.Inject

class MovieRepository @Inject constructor(
        @Stub private val movieDataSource: MovieDataSource,
        private val movieDao: MovieDao,
        private val errorMapper: ErrorMapper
) : BaseRepository(errorMapper) {

    suspend fun getAllMovies(): Either<Error, List<Movie>> {
        // at first step we will check if any movie exists on db or not
        return if (movieDao.getCount() == 0) {
            // nothing exists on database, so get them from cloud
            val either = safeApiCall(movieDataSource::getAllMovies)
            if (either is Right) {
                movieDao.insertMovies(either.b)
            }
            either
        } else {
            // there are some movies in db, so attempt to get them
            Companion.right(movieDao.getAllMovies())
        }
    }
}