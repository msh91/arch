package io.github.msh91.arch.ui.home.list

import android.util.Log
import io.github.msh91.arch.domain.model.movie.Movie
import io.github.msh91.arch.domain.model.response.SuccessResponse
import io.github.msh91.arch.domain.model.response.UseCaseResponse
import io.github.msh91.arch.domain.usecase.movie.GetAllMoviesUseCase
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.livedata.NonNullLiveData
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        connectionManager: BaseConnectionManager,
        private val getAllMoviesUseCase: GetAllMoviesUseCase
) : BaseViewModel(connectionManager) {
    private val TAG = HomeListViewModel::class.java.simpleName

    val movies = NonNullLiveData<List<Movie>>(emptyList())

    init {
        getAllMoviesUseCase
                .execute(compositeDisposable, this::getAllMoviesResponse, this::onTokenExpired)
    }

    private fun getAllMoviesResponse(response: UseCaseResponse<List<Movie>>) {
        Log.d(TAG, "getAllMoviesResponse() called  with: response = [$response]")
        when (response) {
            is SuccessResponse -> movies.value = response.value
        }
    }

    fun onItemClicked(movie: Movie) {
        Log.d(TAG, "onItemClicked() called  with: movie = [$movie]")
    }
}