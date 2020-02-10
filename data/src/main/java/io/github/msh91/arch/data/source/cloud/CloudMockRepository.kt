package io.github.msh91.arch.data.source.cloud

import com.google.gson.Gson
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.source.local.file.BaseFileProvider
import io.github.msh91.arch.data.util.fromJson

/**
 * Mock implementation of [BaseCloudRepository].
 *
 * <b>CAUTION : this implementation should be used only in debug mode</b>
 */
class CloudMockRepository(
        private val gson: Gson,
        private val fileProvider: BaseFileProvider
) : BaseCloudRepository {

    override suspend fun getAllMovies(): List<Movie> {
        val inputStream = fileProvider.getAsset("movies.json")
        val response = String(fileProvider.getByteArrayFromInputStream(inputStream))
        return gson.fromJson<List<Movie>>(response) ?: emptyList()
    }
}