package io.github.msh91.arch.domain

import android.support.annotation.VisibleForTesting
import io.github.msh91.arch.data.model.error.ErrorModel
import io.github.msh91.arch.util.ErrorUtil
import io.reactivex.Single
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

    abstract fun buildUseCaseObservable(): Single<T>

    fun execute(onResponse: (model: T?, errorModel: ErrorModel?) -> Unit, onTokenExpire: (() -> Unit)? = null): Disposable {
        return this.buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onResponse(it, null) },
                        {
                            val error = errorUtil.getErrorModel(it)

                            /*if (error.errorStatus == ErrorStatus.UNAUTHORIZED)
                                onTokenExpire()
                            else {
                            }*/

                            onResponse(null, error)
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
