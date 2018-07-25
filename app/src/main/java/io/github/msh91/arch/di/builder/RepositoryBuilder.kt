package io.github.msh91.arch.di.builder

import dagger.Binds
import dagger.Module
import io.github.msh91.arch.data.repository.movie.MovieRepositoryImpl
import io.github.msh91.arch.domain.repository.MovieRepository

@Module
abstract class RepositoryBuilder {
    @Binds
    abstract fun bindsMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository
}