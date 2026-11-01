package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that emits [initial] and then the result of applying [accumulator]
 * to the previous result and each element.
 * @param initial initial accumulator value, emitted first.
 * @param accumulator function that combines the current accumulator value with an element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> LiveData<T>.scan(
    initial: R,
    accumulator: (acc: R, value: T) -> R,
): LiveData<R> {
    val result = MediatorLiveData(initial)
    var acc = initial
    result.addSource(this) { value ->
        acc = accumulator(acc, value)
        result.value = acc
    }
    return result
}

/**
 * Returns new [LiveData] instance that emits the first element and then the result of applying
 * [accumulator] to the previous result and each following element.
 * @param accumulator function that combines the current accumulator value with an element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.scan(accumulator: (acc: T, value: T) -> T): LiveData<T> {
    val result = MediatorLiveData<T>()
    var acc: Any? = NOT_SET
    result.addSource(this) { value ->
        acc = if (acc === NOT_SET) value else accumulator(acc as T, value)
        result.value = acc as T
    }
    return result
}
