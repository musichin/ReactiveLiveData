package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that invokes [onValue] for each element, before the element is passed to observers.
 * @param onValue function to invoke with each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.doOnValue(onValue: (T) -> Unit): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        onValue(value)
        result.value = value
    }
    return result
}

/**
 * Returns new [LiveData] instance that invokes [onActive] whenever it becomes active.
 * @param onActive function to invoke.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.doOnActive(onActive: () -> Unit): LiveData<T> {
    val hook =
        object : LiveData<T>() {
            override fun onActive() = onActive()
        }

    val result = MediatorLiveData<T>()
    result.addSource(this) { result.value = it }
    result.addSource(hook) { }
    return result
}

/**
 * Returns new [LiveData] instance that invokes [onInactive] whenever it becomes inactive.
 * @param onInactive function to invoke.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.doOnInactive(onInactive: () -> Unit): LiveData<T> {
    val hook =
        object : LiveData<T>() {
            override fun onInactive() = onInactive()
        }

    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> result.value = value }
    result.addSource(hook) { }
    return result
}
