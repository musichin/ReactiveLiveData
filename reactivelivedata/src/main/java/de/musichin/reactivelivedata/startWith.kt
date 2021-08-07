package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that starts with given element.
 * @return new [LiveData] instance.
 */
fun <T> LiveData<T>.startWith(value: T): LiveData<T> =
    startWith(liveData(value))

/**
 * Returns new [LiveData] instance that starts with an element
 * obtained by calling given function when it gets active.
 * @param value function to invoke to obtain the value.
 * @return new [LiveData] instance.
 */
fun <T> LiveData<T>.startWith(value: () -> T): LiveData<T> =
    startWith(liveData(value))

/**
 * Returns new [LiveData] instance that starts with an element
 * obtained by given [LiveData].
 * @param value obtains first of this [LiveData].
 * @return new [LiveData] instance.
 */
fun <T> LiveData<T>.startWith(value: LiveData<T>): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(value) { valueStart ->
        result.removeSource(value)
        result.value = valueStart
        result.addSource(this) { valueMain ->
            result.value = valueMain
        }
    }
    return result
}
