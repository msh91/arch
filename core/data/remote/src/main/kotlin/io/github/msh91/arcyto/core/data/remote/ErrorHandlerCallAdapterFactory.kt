package io.github.msh91.arcyto.core.data.remote

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * A [CallAdapter.Factory] that supports Kotlin.Result as api return type
 */
class ErrorHandlerCallAdapterFactory @Inject constructor() : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation?>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType) || returnType !is ParameterizedType) {
            return null
        }
        val upperBound = getParameterUpperBound(0, returnType)

        return if (upperBound is ParameterizedType && upperBound.rawType == Result::class.java) {
            object : CallAdapter<Any, Call<Result<*>>> {
                override fun responseType(): Type = getParameterUpperBound(0, upperBound)

                @Suppress("UNCHECKED_CAST")
                override fun adapt(call: Call<Any>): Call<Result<*>> =
                    ResultCall(call) as Call<Result<*>>
            }
        } else {
            null
        }
    }

}

private class ResultCall<T>(val delegate: Call<T>) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                response.code(),
                                Result.success(response.body()!!),
                            ),
                        )
                    } else {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(HttpException(response))),
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.failure(t)),
                    )
                }
            },
        )
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun execute(): Response<Result<T>> = Response.success(Result.success(delegate.execute().body()!!))

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
