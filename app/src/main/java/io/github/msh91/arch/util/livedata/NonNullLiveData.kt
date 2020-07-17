package io.github.msh91.arch.util.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * A wrapper class for [MutableLiveData] that does not accept or return null values.
 * The [defaultValue] causes all observers to observe data at least one time
 *
 * @param defaultValue will be set to [MutableLiveData.setValue] at the first time, and after that
 * all null values in [getValue], [setValue] and [observe] will be replaced with default value
 */
class NonNullLiveData<T>(private val defaultValue: T) : MutableLiveData<T>() {
    init {
        value = defaultValue
    }

    /**
     * @return returns current value or [defaultValue] if current value is null
     */
    override fun getValue(): T = super.getValue() ?: defaultValue

    /**
     * @param value will be set if it is not null, otherwise [defaultValue] will be replaced.
     */
    override fun setValue(value: T?) {
        super.setValue(value ?: defaultValue)
    }

    /**
     * @param owner an instance of [LifecycleOwner] to observe on.
     * @param body a lambda function that will be invoked with value if it is not null, otherwise
     * [defaultValue] will be replaced.
     */
    fun observe(owner: LifecycleOwner, body: (T) -> Unit) {
        observe(owner, Observer { body(it ?: defaultValue) })
    }

    fun observe(body: (T) -> Unit) {
        observeForever { body(it ?: defaultValue) }
    }

    /**
     * @param value will be set if it is not null, otherwise [defaultValue] will be replaced.
     */
    override fun postValue(value: T?) {
        super.postValue(value ?: defaultValue)
    }
}
