package io.github.msh91.arch.util.extension

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.github.msh91.arch.util.NonNullLiveData
import io.reactivex.Flowable
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

fun <T, L : LiveData<T>> LifecycleOwner.observe(l: L, observer: (T?) -> Unit) {
    l.observe(this, Observer {
        observer(it)
    })
}

fun <T> LifecycleOwner.observe(l: NonNullLiveData<T>, observer: (T) -> Unit) {
    l.observe(this, { observer(it) })
}

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement): T? = this.fromJson<T>(jsonElement, object : com.google.gson.reflect.TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(jsonStr: String): T? = this.fromJson<T>(jsonStr, object : com.google.gson.reflect.TypeToken<T>() {}.type)


fun <T : Fragment> T.setArguments(vararg params: Pair<String, Any?>): T {
    val args = Bundle()
    if (params.isNotEmpty()) args.fill(*params)
    arguments = args
    return this
}

inline fun <reified T> Bundle.getGenericSerializable(key: String) = getSerializable(key) as T

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
/*

*/
/**
 * Converts LiveData into a Flowable
 */

fun <T> LiveData<T>.toFlowable(lifecycleOwner: LifecycleOwner): Flowable<T> =
        Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))

fun Activity.closeKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.closeKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Date.timeString() = SimpleDateFormat("HH:mm:ss", Locale("fa", "IR"))
        .let { it.format(this)!! }
/**
 * Converts a Flowable into LiveData
 *//*

fun <T> Flowable<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)*/
