package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that starts with given element.
 * @param value element to start with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.startWith(value: T): LiveData<T> = startWith(liveData(value))

/**
 * Returns new [LiveData] instance that starts with given elements.
 * Only the observer that activates the returned [LiveData] receives every element;
 * observers added later receive only the latest one.
 * Without elements, it emits the same elements as current source.
 * @param values elements to start with, in emission order.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.startWith(vararg values: T): LiveData<T> {
    if (values.isEmpty()) {
        val result = MediatorLiveData<T>()
        result.addSource(this) { value -> result.value = value }
        return result
    }
    return values.foldRight(this) { value, source -> source.startWith(value) }
}

/**
 * Returns new [LiveData] instance that starts with an element
 * obtained by calling given function when it first becomes active.
 * @param value function to invoke to obtain the element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.startWith(value: () -> T): LiveData<T> = startWith(liveData(value))

/**
 * Returns new [LiveData] instance that starts with the first element emitted by [other].
 * @param other [LiveData] whose first element is emitted first.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.startWith(other: LiveData<T>): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(other) { valueStart ->
        result.removeSource(other)
        result.value = valueStart
        result.addSource(this) { valueMain ->
            result.value = valueMain
        }
    }
    return result
}
