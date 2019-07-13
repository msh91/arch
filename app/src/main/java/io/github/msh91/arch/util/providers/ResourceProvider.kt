package io.github.msh91.arch.util.providers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.io.InputStream
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

    override fun getAsset(fileName: String): InputStream {
        return mContext.assets.open(fileName)
    }

    override fun getContentInputStream(uri: Uri): InputStream {
        return mContext.contentResolver.openInputStream(uri)
    }

    override fun getMimType(uri: Uri): String {
        return if (uri.scheme?.contentEquals("content") == true)
            return mContext.contentResolver.getType(uri)
        else
            MimeTypeMap.getFileExtensionFromUrl(uri.path)
                    ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(uri.path)
    }

    override fun getContentResolver(): ContentResolver = mContext.contentResolver

    override fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(mContext, drawableId)!!
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate()
        }
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
