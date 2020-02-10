package io.github.msh91.arch.data.repository

import io.github.msh91.arch.data.model.response.Result
import io.github.msh91.arch.data.model.response.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.withContext

abstract class BaseRepository(
        private val dispatcher: CoroutineDispatcher,
        private val exceptionHandler: CoroutineExceptionHandler
) {
    suspend fun <T> safeCall(call: suspend () -> T): Result<T> {
        return withContext(dispatcher + exceptionHandler) {
            Success(call.invoke())
        }

    }
}