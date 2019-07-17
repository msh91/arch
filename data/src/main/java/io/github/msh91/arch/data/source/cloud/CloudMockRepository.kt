package io.github.msh91.arch.data.source.cloud

import com.google.gson.Gson
import io.github.msh91.arch.data.source.local.file.BaseFileProvider
import io.github.msh91.arch.data.util.fromJson
import io.github.msh91.arch.domain.model.movie.Movie
import io.reactivex.Flowable

/**
 * Mock implementation of [BaseCloudRepository].
 *
 * <b>CAUTION : this implementation should be used only in debug mode</b>
 */
class CloudMockRepository(
        private val gson: Gson,
        private val fileProvider: BaseFileProvider
) : BaseCloudRepository {

    override fun getAllMovies(): Flowable<List<Movie>> {
        return Flowable.fromCallable {
            val inputStream = fileProvider.getAsset("movies.json")
            val response = String(fileProvider.getByteArrayFromInputStream(inputStream))
            gson.fromJson<List<Movie>>(response)
        }
    }
}