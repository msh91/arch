package io.github.msh91.arch.data.mapper

import com.google.gson.Gson
import io.github.msh91.arch.data.model.response.ErrorModel
import io.github.msh91.arch.data.model.response.ErrorStatus
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class CloudErrorMapper @Inject constructor(private val gson: Gson) {

    fun mapToErrorModel(throwable: Throwable): ErrorModel? {
        return when (throwable) {
            is HttpException -> {
                getHttpError(throwable)
            }
            is SocketTimeoutException -> {
              ErrorModel(ErrorStatus.TIMEOUT)
            }
            is IOException -> {
              ErrorModel(ErrorStatus.NO_CONNECTION)
            }
            else -> null
        }
    }

    private fun getHttpError(httpException: HttpException): ErrorModel {
        val code = httpException.code()
        val status = if (code == 401) ErrorStatus.UNAUTHORIZED else ErrorStatus.BAD_RESPONSE
        return ErrorModel(null, code, status)
    }
}