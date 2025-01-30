package io.github.msh91.arcyto.core.data.remote

import android.content.Context
import com.squareup.anvil.annotations.ContributesMultibinding
import io.github.msh91.arcyto.core.di.common.ErrorMapper
import io.github.msh91.arcyto.core.di.scope.AppScope
import jakarta.inject.Inject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ContributesMultibinding(AppScope::class)
class RemoteErrorMapper @Inject constructor(context: Context) : ErrorMapper {
    private val resources = context.resources

    override fun getErrorMessage(exception: Throwable): String? {
        return when (exception) {
            is SocketTimeoutException -> resources.getString(R.string.error_request_timed_out)
            is UnknownHostException -> resources.getString(R.string.error_no_internet_connection)
            is HttpException -> {
                when (exception.code()) {
                    500 -> resources.getString(R.string.error_server_error)
                    404 -> resources.getString(R.string.error_resource_not_found)
                    else -> resources.getString(R.string.error_server_unknown, exception.code())
                }
            }

            else -> null
        }
    }
}