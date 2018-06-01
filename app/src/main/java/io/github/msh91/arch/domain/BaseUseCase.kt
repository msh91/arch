package io.github.msh91.arch.domain

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
 * Should be used as parent class of all UseCases.
 * it will execute provided [Flowable] by child UseCase, subscribe to it and invoke the callback
 */
abstract class BaseUseCase<T>(private val errorUtil: ErrorUtil) {

    /**
     * should be override by child UseCase and will return desired [Flowable] created by repositories
     */
    abstract fun buildUseCaseObservable(): Flowable<T>

    /**
     * subscribed to [Flowable] that returns from [buildUseCaseObservable] and send response via lambda callback.
     *
     * @param compositeDisposable an instance of [compositeDisposable] to add created disposable to it
     * @param onResponse a lambda function that receives a [APIResponse] in order to invoke when result is available or an error occurred
     * @param onTokenExpire a nullable lambda function to invoke when token expired and server returned UNAUTHORIZED
     *
     * @return returns created disposable
     */
    fun execute(
            compositeDisposable: CompositeDisposable,
            onResponse: (APIResponse<T>) -> Unit,
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
}
