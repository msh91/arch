package io.github.msh91.arch.util.providers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
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

    override fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(mContext, resId)
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

    override fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(mContext, drawableId)!!
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
