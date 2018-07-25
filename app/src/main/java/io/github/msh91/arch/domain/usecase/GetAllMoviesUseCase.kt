package io.github.msh91.arch.domain.usecase

import io.github.msh91.arch.domain.BaseUseCase
import io.github.msh91.arch.domain.mapper.DomainErrorUtil
import io.github.msh91.arch.domain.model.movie.Movie
import io.github.msh91.arch.domain.repository.MovieRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
        errorUtil: DomainErrorUtil,
        private val movieRepository: MovieRepository
) : BaseUseCase<List<Movie>>(errorUtil) {

    override fun buildUseCaseObservable(): Flowable<List<Movie>> {
        return movieRepository.getAllMovies()
    }
}