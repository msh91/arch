package io.github.msh91.arch.domain

import android.support.annotation.VisibleForTesting
import io.github.msh91.arch.data.model.response.APIResponse
import io.github.msh91.arch.data.model.response.ErrorResponse
import io.github.msh91.arch.data.model.response.SuccessResponse
import io.github.msh91.arch.data.model.response.error.ErrorStatus
import io.github.msh91.arch.util.ErrorUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by m.aghajani on 2/15/2018.
 */
abstract class BaseUseCase<T>(
        private val errorUtil: ErrorUtil
) {


    val disposables: CompositeDisposable = CompositeDisposable()

    abstract fun buildUseCaseObservable(): Flowable<T>

    fun execute(onResponse: (APIResponse<T>) -> Unit, onTokenExpire: (() -> Unit)? = null): Disposable {
        return this.buildUseCaseObservable()
                .onBackpressureLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onResponse(SuccessResponse(it)) },
                        {
                            val error = errorUtil.getErrorModel(it)

                            if (error.errorStatus == ErrorStatus.UNAUTHORIZED)
                                onTokenExpire?.invoke()

                            onResponse(ErrorResponse(error))
                        }).also { addDisposable(it) }
    }

    fun dispose(disposable: Disposable) {
        disposables.remove(disposable)
    }

    fun dispose() {
        disposables.dispose()
    }

    @VisibleForTesting
    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}
