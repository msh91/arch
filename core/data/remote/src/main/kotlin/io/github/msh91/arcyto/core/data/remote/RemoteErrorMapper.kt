package io.github.msh91.arcyto.core.data.remote

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import io.github.msh91.arcyto.core.data.local.resource.StringProvider
import io.github.msh91.arcyto.core.di.common.ErrorMapper
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Inject
@ContributesIntoSet(AppScope::class)
class RemoteErrorMapper(
    private val stringProvider: StringProvider,
) : ErrorMapper {
    override fun getErrorMessage(exception: Throwable): String? =
        when (exception) {
            is SocketTimeoutException -> {
                stringProvider.getString(R.string.error_request_timed_out)
            }

            is UnknownHostException -> {
                stringProvider.getString(R.string.error_no_internet_connection)
            }

            is HttpException -> {
                when (exception.code()) {
                    500 -> stringProvider.getString(R.string.error_server_error)
                    404 -> stringProvider.getString(R.string.error_resource_not_found)
                    else -> stringProvider.getString(R.string.error_server_unknown, exception.code())
                }
            }

            else -> {
                null
            }
        }
}
