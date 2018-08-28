package io.github.msh91.arch.domain.usecase.base

import io.github.msh91.arch.domain.mapper.DomainErrorUtil
import io.github.msh91.arch.domain.model.response.ErrorModel
import io.github.msh91.arch.domain.model.response.ErrorStatus
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class CompletableUseCase(private val errorUtil: DomainErrorUtil) : UseCase<Completable>() {
    /**
     * subscribed to [Completable] instance that returns from [execute] and notify caller via a lambda callback.
     *
     * @param compositeDisposable an instance of [compositeDisposable] to add created disposable to it.
     * @param onSuccess a lambda function that will be invoked when useCase executed successfully.
     * @param onFailure a lambda function that will be invoked when an error occurred while useCase executed.
     * @param onTokenExpire a nullable lambda function to invoke when token expired and repository returned [ErrorStatus.UNAUTHORIZED].
     *
     * @return returns created disposable
     */
    fun execute(
            compositeDisposable: CompositeDisposable,
            onSuccess: () -> Unit,
            onFailure: (ErrorModel) -> Unit,
            onTokenExpire: (() -> Unit)? = null
    ): Disposable {
        return this.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onSuccess()
                        },
                        {
                            val error = errorUtil.getErrorModel(it)

                            if (error.errorStatus == ErrorStatus.UNAUTHORIZED) {
                                onTokenExpire?.invoke()
                            }
                            onFailure(error)

                        }).also { compositeDisposable.add(it) }
    }

    /**
     * Same as [execute] but also will receive an object as requestModel, and pass it to callback
     * via result.
     *
     * It can be used in situations that you want to call execute multiple times, so you can specify
     * each execution with a requestModel, e.g you can pass an [Int] for requestModel and use it
     * as a request id.
     */
    fun <R> executeAndKeepRequest(
            compositeDisposable: CompositeDisposable,
            requestModel: R,
            onSuccess: (requestModel: R) -> Unit,
            onFailure: (ErrorModel, requestModel: R) -> Unit,
            onTokenExpire: (() -> Unit)? = null
    ): Disposable {
        return this.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onSuccess(requestModel)
                        },
                        {
                            val error = errorUtil.getErrorModel(it)

                            if (error.errorStatus == ErrorStatus.UNAUTHORIZED) {
                                onTokenExpire?.invoke()
                            }
                            onFailure(error, requestModel)
                        })
                .also { compositeDisposable.add(it) }
    }
}