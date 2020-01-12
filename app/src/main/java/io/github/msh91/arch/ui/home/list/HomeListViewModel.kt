package io.github.msh91.arch.ui.home.list

import android.util.Log
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.movie.Movie
import io.github.msh91.arch.data.model.response.ErrorResponse
import io.github.msh91.arch.data.model.response.ErrorStatus
import io.github.msh91.arch.data.model.response.SuccessResponse
import io.github.msh91.arch.data.model.response.UseCaseResponse
import io.github.msh91.arch.data.repository.movie.MovieRepository
import io.github.msh91.arch.ui.base.BaseViewModel
import io.github.msh91.arch.util.connectivity.BaseConnectionManager
import io.github.msh91.arch.util.livedata.NonNullLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeListViewModel @Inject constructor(
        connectionManager: BaseConnectionManager,
        private val movieRepository: MovieRepository,
        private val errorMapper: ErrorMapper
) : BaseViewModel(connectionManager) {
    private val TAG = HomeListViewModel::class.java.simpleName

    val movies = NonNullLiveData<List<Movie>>(emptyList())

    init {
        movieRepository
            .getAllMovies()
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getAllMoviesResponse(SuccessResponse(it))
                },
                {
                    val error = errorMapper.getErrorModel(it)

                    if (error.errorStatus == ErrorStatus.UNAUTHORIZED) {
                        onTokenExpired()
                    }
                    getAllMoviesResponse(ErrorResponse(error))

                }).also { compositeDisposable.add(it) }
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