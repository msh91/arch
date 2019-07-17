package io.github.msh91.arch.util.providers

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Resolves application's resources.
 */
interface BaseResourceProvider {

    /**
     * Resolves text's id to String.
     *
     * @param id to be fetched from the resources
     * @return String representation of the {@param id}
     */
    fun getString(@StringRes id: Int): String

    /**
     * Resolves text's id to String and formats it.
     *
     * @param resId      to be fetched from the resources
     * @param formatArgs format arguments
     * @return String representation of the {@param resId}
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    /**
     * Resolves color's id to int
     *
     * @param resId to ne fetched from the resources
     * @return Int representation of the {@param id}
     */
    fun getColor(@ColorRes resId: Int): Int

    /**
     * Resolves drawable's id to Drawable
     *
     * @param resId to ne fetched from the resources
     * @return Drawable representation of the {@param id}
     */
    fun getDrawable(@DrawableRes resId: Int): Drawable?

    /**
     * get an instance of [Bitmap] from given vector resource
     */
    fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap
}
