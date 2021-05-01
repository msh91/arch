package io.github.msh91.arch.util.provider

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import io.github.msh91.arch.R
import io.github.msh91.arch.data.model.Error
import io.github.msh91.arch.data.model.HttpError
import javax.inject.Inject

/**
 * Concrete implementation of the [BaseResourceProvider] interface.
 */
class ResourceProvider @Inject constructor(context: Context) : BaseResourceProvider {

    private val mContext: Context = context

    override fun getString(@StringRes id: Int): String {
        return mContext.getString(id)
    }

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return mContext.getString(resId, *formatArgs)
    }

    override fun getColor(resId: Int): Int {
        return ContextCompat.getColor(mContext, resId)
    }

    override fun getErrorMessage(error: Error): String {
        return when (error) {
            HttpError.ConnectionFailed -> getString(R.string.error_connection)
            is HttpError.InvalidResponse -> error.message ?: getString(R.string.error_not_defined)
            HttpError.TimeOut -> getString(R.string.error_timeout)
            HttpError.UnAuthorized -> getString(R.string.error_unauthorized)
            is Error.NotDefined -> getString(R.string.error_not_defined)
            Error.Null -> getString(R.string.error_null)
        }

    }
}
