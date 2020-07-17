package io.github.msh91.arch.data.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import arrow.core.Either
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.github.msh91.arch.data.model.Error

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement): T? {
    return this.fromJson<T>(jsonElement, object : com.google.gson.reflect.TypeToken<T>() {}.type)
}

inline fun <reified T> Gson.fromJson(jsonStr: String): T? {
    return this.fromJson<T>(jsonStr, object : com.google.gson.reflect.TypeToken<T>() {}.type)
}

fun <T> LiveData<T>.toEither(): LiveData<Either<Error, T>> {
    return Transformations.map(this) { input -> Either.Right(input) }
}
