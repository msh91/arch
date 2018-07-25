package io.github.msh91.arch.data.source.cloud

import io.github.msh91.arch.domain.model.movie.Movie
import io.reactivex.Flowable

/**
 * Mock implementation of [BaseCloudRepository].
 *
 * <b>CAUTION : this implementation should be used only in debug mode</b>
 */
class CloudMockRepository : BaseCloudRepository {

    override fun getAllMovies(): Flowable<List<Movie>> {
        return Flowable.just(listOf(
                Movie(1, "Batman Begins!"),
                Movie(2, "Batman: The Dark Knight"),
                Movie(3, "Batman: The Dark Knight Rises")
        ))
    }
}