package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that skips first elements until [predicate] returns `true`.
 * The element for which [predicate] returns `true` is emitted.
 * @param predicate function to test each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.skipUntil(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    var drop = true
    result.addSource(this) { value ->
        if (!drop || predicate(value)) {
            drop = false
            result.value = value
        }
    }
    return result
}

/**
 * Returns new [LiveData] instance that skips elements until [other] emits.
 * If [other] already holds a value when the returned [LiveData] becomes active, no element is skipped.
 * @param other source whose first emission stops skipping elements.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.skipUntil(other: LiveData<*>): LiveData<T> {
    val result = MediatorLiveData<T>()
    var drop = other !== this
    if (drop) {
        result.addSource(other) {
            drop = false
            result.removeSource(other)
        }
    }
    result.addSource(this) { value ->
        if (!drop) result.value = value
    }
    return result
}

/**
 * Returns new [LiveData] instance that skips first elements as long as [predicate] returns `true`.
 * @param predicate function to test each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.skipWhile(predicate: (T) -> Boolean): LiveData<T> = skipUntil { !predicate(it) }

/**
 * Returns new [LiveData] instance that skips first [count] elements.
 * Only elements received while the returned [LiveData] is active are counted.
 * @param count number of elements to skip, must not be negative.
 * @return new [LiveData] instance.
 * @throws IllegalArgumentException if [count] is negative.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    require(count >= 0) { "count $count must not be negative." }
    var counter = 0
    return skipUntil { ++counter > count }
}

/**
 * Returns new [LiveData] instance that skips last [count] elements by delaying every element
 * until [count] further elements have been received.
 * Only elements received while the returned [LiveData] is active are counted.
 * @param count number of last elements to skip, must not be negative.
 * @return new [LiveData] instance.
 * @throws IllegalArgumentException if [count] is negative.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.skipLast(count: Int): LiveData<T> {
    require(count >= 0) { "count $count must not be negative." }
    val result = MediatorLiveData<T>()
    val buffer = ArrayDeque<T>(count + 1)
    result.addSource(this) { value ->
        buffer.addLast(value)
        if (buffer.size > count) result.value = buffer.removeFirst()
    }
    return result
}
