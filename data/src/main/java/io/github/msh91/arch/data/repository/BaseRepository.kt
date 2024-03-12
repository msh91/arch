package io.github.msh91.arch.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.msh91.arch.data.mapper.ErrorMapper
import io.github.msh91.arch.data.model.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository(private val errorMapper: ErrorMapper) {
    protected suspend fun <T : Any> safeApiCall(call: suspend () -> T): Either<Error, T> {
        return withContext(Dispatchers.IO) {
            getResult(call)
        }
    }

    private suspend fun <T : Any> getResult(call: suspend () -> T): Either<Error, T> {
        return try {
            call.invoke().right()
        } catch (t: Throwable) {
            errorMapper.getError(t).left()
        }
    }
}
