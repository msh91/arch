package io.github.msh91.arch.domain.usecase.movie

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.msh91.arch.domain.mapper.DomainErrorUtil
import io.github.msh91.arch.domain.model.movie.Movie
import io.github.msh91.arch.domain.repository.MovieRepository
import io.reactivex.Flowable
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllMoviesUseCaseTest {
    @Mock
    lateinit var errorUtil: DomainErrorUtil

    @Mock
    lateinit var movieRepository: MovieRepository

    @InjectMocks
    lateinit var getAllMoviesUseCase: GetAllMoviesUseCase

    @Test
    fun `execute should get a flowable instance from repository`() {
        // GIVEN
        val mockedFlowable = mock<Flowable<List<Movie>>>()
        whenever(movieRepository.getAllMovies()).thenReturn(mockedFlowable)

        // WHEN
        val result = getAllMoviesUseCase.execute()

        // THEN
        assertThat(result, `is`(mockedFlowable))
    }
}