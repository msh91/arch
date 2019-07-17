package io.github.msh91.arch.data.util

import com.google.gson.Gson
import com.google.gson.JsonElement

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement): T? = this.fromJson<T>(jsonElement, object : com.google.gson.reflect.TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(jsonStr: String): T? = this.fromJson<T>(jsonStr, object : com.google.gson.reflect.TypeToken<T>() {}.type)