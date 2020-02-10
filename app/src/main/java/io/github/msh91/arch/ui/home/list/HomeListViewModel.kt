package io.github.msh91.arch.ui.home.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.model.response.Result
import io.github.msh91.arch.data.model.response.Success
import io.github.msh91.arch.data.repository.movie.MovieRepository
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.livedata.NonNullLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        connectionManager: BaseConnectionManager,
        private val movieRepository: MovieRepository,
        private val errorMapper: ErrorMapper
) : BaseViewModel(connectionManager) {
    private val TAG = HomeListViewModel::class.java.simpleName

    val movies = NonNullLiveData<List<Movie>>(emptyList())

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            getAllMoviesResponse(movieRepository.getAllMovies())
        }
    }

    private fun getAllMoviesResponse(response: Result<List<Movie>>) {
        Log.d(TAG, "getAllMoviesResponse() called  with: response = [$response]")
        when (response) {
            is Success -> movies.value = response.value
        }
    }

    fun onItemClicked(movie: Movie) {
        Log.d(TAG, "onItemClicked() called  with: movie = [$movie]")
    }
}