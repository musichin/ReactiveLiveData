package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Invokes specified function when new value is emitted.
 * @param onValue function to invoke.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.doOnValue(onValue: (T) -> Unit): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        result.value = value
        onValue(value)
    }
    return result
}

/**
 * Invokes specified function when [LiveData] gets active.
 * @param onActive function to invoke.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.doOnActive(onActive: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onActive() = onActive()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { result.value = it }
    result.addSource(hook) { }
    return result
}

/**
 * Invokes specified function when [LiveData] gets inactive.
 * @param onInactive function to invoke.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.doOnInactive(onInactive: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onInactive() = onInactive()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> result.value = value }
    result.addSource(hook) { }
    return result
}
