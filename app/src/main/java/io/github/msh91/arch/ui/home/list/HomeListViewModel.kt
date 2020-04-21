package io.github.msh91.arch.ui.home.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.model.response.Error
import io.github.msh91.arch.data.repository.movie.MovieRepository
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.livedata.NonNullLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        private val movieRepository: MovieRepository,
        errorMapper: ErrorMapper
) : BaseViewModel() {
    private val TAG = HomeListViewModel::class.java.simpleName

    val movies = NonNullLiveData<List<Movie>>(emptyList())

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        viewModelScope.launch {
            when (val either = movieRepository.getAllMovies()) {
                is Right -> movies.value = either.b
                is Left -> showError(either.a)
            }
        }
    }

    private fun showError(error: Error) {
        Log.d(TAG, "showError() called  with: error = [$error]")
    }

    fun onItemClicked(movie: Movie) {
        Log.d(TAG, "onItemClicked() called  with: movie = [$movie]")
    }
}