package io.github.msh91.arch.util.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

/**
 * A wrapper class for {@link MutableLiveData} that does not accept null values
 */
class NonNullLiveData<T>(private val defaultValue: T) : MutableLiveData<T>() {
    init {
        value = defaultValue
    }

    override fun getValue(): T = super.getValue() ?: defaultValue

    override fun setValue(value: T?) {
        super.setValue(value ?: defaultValue)
    }

    fun observe(owner: LifecycleOwner, body: (T) -> Unit) {
        observe(owner, Observer { body(it ?: defaultValue) })
    }

    fun observe(body: (T) -> Unit) {
        observeForever { body(it ?: defaultValue) }
    }

    override fun postValue(value: T) {
        super.postValue(value)
    }
}