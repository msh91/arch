package io.github.msh91.arch.domain

import io.github.msh91.arch.domain.mapper.DomainErrorUtil
import io.github.msh91.arch.domain.model.response.ErrorResponse
import io.github.msh91.arch.domain.model.response.ErrorStatus
import io.github.msh91.arch.domain.model.response.SuccessResponse
import io.github.msh91.arch.domain.model.response.UseCaseResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Should be used as parent class of all UseCases.
 * it will execute provided [Flowable] by child UseCase, subscribe to it and invoke the callback
 */
abstract class BaseUseCase<T>(private val errorUtil: DomainErrorUtil) {

    /**
     * should be override by child UseCase and will return desired [Flowable] created by repositories
     */
    abstract fun buildUseCaseObservable(): Flowable<T>

    /**
     * subscribed to [Flowable] that returns from [buildUseCaseObservable] and send response via lambda callback.
     *
     * @param compositeDisposable an instance of [compositeDisposable] to add created disposable to it
     * @param onResponse a lambda function that receives a [UseCaseResponse] in order to invoke when result is available or an error occurred
     * @param onTokenExpire a nullable lambda function to invoke when token expired and repository returned UNAUTHORIZED
     *
     * @return returns created disposable
     */
    fun execute(
            compositeDisposable: CompositeDisposable,
            onResponse: (UseCaseResponse<T>) -> Unit,
            onTokenExpire: (() -> Unit)? = null
    ): Disposable {
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
            onResponse: (response: UseCaseResponse<T>, request: R) -> Unit,
            onTokenExpire: () -> Unit
    ): Disposable {
        return this.buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onResponse(SuccessResponse(it), requestModel) },
                        {
                            val error = errorUtil.getErrorModel(it)

                            if (error.errorStatus == ErrorStatus.UNAUTHORIZED)
                                onTokenExpire()
                            else {
                                onResponse(ErrorResponse(error), requestModel)
                            }
                        })
                .also { compositeDisposable.add(it) }
    }
}
