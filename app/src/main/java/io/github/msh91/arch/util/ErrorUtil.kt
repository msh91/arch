package io.github.msh91.arch.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.msh91.arch.data.model.response.error.ErrorModel
import io.github.msh91.arch.data.model.response.error.ErrorStatus
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * A util class that generate an instance of [ErrorModel] with happened [Throwable]
 * @param gson an instance of [Gson] to parse error body from [ResponseBody]
 */
class ErrorUtil
@Inject constructor(val gson: Gson) {

    /**
     * Generate an instance of [ErrorModel] with happened [Throwable]
     * @param t happened [Throwable]
     *
     * @return rentuns an instance of [ErrorModel]
     */
    fun getErrorModel(t: Throwable?): ErrorModel {
        // if throwable is null then return a NOT_DEFINED error
        if (t == null) {
            return ErrorModel(ErrorStatus.NOT_DEFINED)
        }

        // if throwable is an instance of HttpException
        // then attempt to parse error data from response body
        if (t is HttpException) {

            // handle UNAUTHORIZED situation (when token expired)
            return if (t.code() == 401) {
                ErrorModel(ErrorStatus.UNAUTHORIZED)
            } else {
                getHttpError(t.response().errorBody())
            }
        }

        // handle api call timeout error
        if (t is SocketTimeoutException) {
            return ErrorModel(ErrorStatus.TIMEOUT)
        }

        // handle connection error
        if (t is IOException) {
            return ErrorModel(ErrorStatus.NO_CONNECTION)
        }

        // if response was successful but no data received
        if (t is NullPointerException) {
            return ErrorModel(ErrorStatus.EMPTY_RESPONSE)
        }

        // something happened that we are not make our self ready for it
        return ErrorModel(ErrorStatus.NOT_DEFINED)
    }

    /**
     * attempts to parse http reponse body and get error data from it
     *
     * @param body retrofit response body
     * @return returns an instance of [ErrorModel] with parsed data or NOT_DEFINED status
     */
    private fun getHttpError(body: ResponseBody?): ErrorModel {
        return try {

            val listType = object : TypeToken<List<ErrorModel>>() {}.type
            val errorModelList: List<ErrorModel> = gson.fromJson(body!!.string(), listType)
            errorModelList[0].also {
                it.errorStatus = ErrorStatus.BAD_RESPONSE
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            ErrorModel(ErrorStatus.NOT_DEFINED)
        }

    }

}