package io.github.msh91.arch.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseRepository(
        private val dispatcher: CoroutineDispatcher
) {
    protected suspend fun <T : Any> safeCall(call: suspend () -> T): T {
        return withContext(dispatcher) {
            call.invoke()
        }
    }
}