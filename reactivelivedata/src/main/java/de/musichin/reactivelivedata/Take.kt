package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that takes only first [count] elements.
 * @param count number of elements to take, must not be negative.
 * @return new [LiveData] instance.
 * @throws IllegalArgumentException if [count] is negative.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.take(count: Int): LiveData<T> {
    require(count >= 0) { "count $count must not be negative." }
    if (count == 0) return liveData()
    var counter = 0
    return takeUntil { ++counter >= count }
}

/**
 * Returns new [LiveData] instance that takes first elements until [predicate] returns `true`.
 * The element for which [predicate] returns `true` is still emitted.
 * @param predicate function to test each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.takeUntil(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value)) result.removeSource(this)
        result.value = value
    }
    return result
}

/**
 * Returns new [LiveData] instance that takes first elements as long as [predicate] returns `true`.
 * @param predicate function to test each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.takeWhile(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value)) {
            result.value = value
        } else {
            result.removeSource(this)
        }
    }
    return result
}

/**
 * Returns new [LiveData] instance that takes only first element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.first(): LiveData<T> = take(1)
