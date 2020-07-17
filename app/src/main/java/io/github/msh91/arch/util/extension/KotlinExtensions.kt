package io.github.msh91.arch.util.extension

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Observe [LiveData] on an instance of [LifecycleOwner] like [Fragment] or [Activity]
 * @param lifecycleOwner
 * @param observer a lambda function that receives a nullable [T] and will be invoked when data is available
 */
fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) {
    this.observe(lifecycleOwner, Observer { observer.invoke(it) })
}

/**
 * Observe [LiveData] on an instance of [LifecycleOwner] like [Fragment] or [Activity] and observer
 * will be invoked  only if emitted value is not null.
 *
 * @param lifecycleOwner
 * @param observer a lambda function that receives a nonnull [T] and will be invoked when data is available
 */
fun <T> LiveData<T>.observeSafe(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(lifecycleOwner, Observer { if (it != null) observer.invoke(it) })
}

/**
 * Add all given values to fragment bundle arguments
 * @param params list of [Pair]s that include key, value for each argument
 */
fun <T : Fragment> T.setArguments(vararg params: Pair<String, Any?>): T {
    val args = Bundle()
    if (params.isNotEmpty()) args.fill(*params)
    arguments = args
    return this
}

/**
 * get [Serializable] from [Bundle] without need to class casting using kotlin 'reified' feature
 */
inline fun <reified T> Bundle.getGenericSerializable(key: String) = getSerializable(key) as T

/**
 * put values into bundle based on their types.
 *
 * @param params list of [Pair]s that include key, value for each argument
 */
fun Bundle.fill(vararg params: Pair<String, Any?>) = apply {
    params.forEach {
        val value = it.second
        when (value) {
            null -> putSerializable(it.first, null as Serializable?)
            is Int -> putInt(it.first, value)
            is Long -> putLong(it.first, value)
            is CharSequence -> putCharSequence(it.first, value)
            is String -> putString(it.first, value)
            is Float -> putFloat(it.first, value)
            is Double -> putDouble(it.first, value)
            is Char -> putChar(it.first, value)
            is Short -> putShort(it.first, value)
            is Boolean -> putBoolean(it.first, value)
            is Serializable -> putSerializable(it.first, value)
            is Bundle -> putBundle(it.first, value)
            is Parcelable -> putParcelable(it.first, value)
            is LongArray -> putLongArray(it.first, value)
            is FloatArray -> putFloatArray(it.first, value)
            is DoubleArray -> putDoubleArray(it.first, value)
            is CharArray -> putCharArray(it.first, value)
            is ShortArray -> putShortArray(it.first, value)
            is BooleanArray -> putBooleanArray(it.first, value)
            is IntArray -> putIntArray(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> putCharSequenceArray(it.first, value as Array<out CharSequence>)
                value.isArrayOf<String>() -> putStringArray(it.first, value as Array<out String>)
                value.isArrayOf<Parcelable>() -> putParcelableArrayList(it.first, value as ArrayList<out Parcelable>)
                else -> throw Exception("Bundle extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            else -> throw Exception("Bundle extra ${it.first} has wrong type ${value.javaClass.name}")
        }
    }
}

/**
 * close keyboard on [Activity.getCurrentFocus] view
 */
fun Activity.closeKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * close keyboard on this view
 */
fun View.closeKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * @return returns formatted time in 'HH:mm:ss' format and in 'fa' locale
 */
fun Date.timeString() = SimpleDateFormat("HH:mm:ss", Locale("fa", "IR"))
    .let { it.format(this)!! }
