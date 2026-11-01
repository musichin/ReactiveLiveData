package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that emits only distinct elements
 * using given key selector result for comparison.
 * Only elements received while the returned [LiveData] is active are considered.
 * @param keySelector function that determines the key of each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T, K> LiveData<T>.distinct(keySelector: (T) -> K): LiveData<T> {
    val keys = hashSetOf<Any?>()
    return filter { keys.add(keySelector(it)) }
}

/**
 * Returns new [LiveData] instance that emits only distinct elements using [Any.equals] for comparison.
 * Only elements received while the returned [LiveData] is active are considered.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> LiveData<T>.distinct(): LiveData<T> = distinct { value -> value }

/**
 * Returns new [LiveData] instance that ensures emitted element is not equal to previous element
 * using given key selector result for comparison.
 * @param keySelector function that determines the key of each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T, K> LiveData<T>.distinctUntilChanged(keySelector: (T) -> K): LiveData<T> {
    var prev: Any? = NOT_SET
    return filter {
        val key = keySelector(it)
        if (key != prev) {
            prev = key
            true
        } else {
            false
        }
    }
}

/**
 * Returns new [LiveData] instance that ensures emitted element is not equal to previous element
 * using [comparer] for comparison.
 * @param comparer called with the previous and the new element; returns `true` if they are equal.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
public fun <T> LiveData<T>.distinctUntilChanged(comparer: (T, T) -> Boolean): LiveData<T> {
    var first = true
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (first || !comparer(result.value as T, value)) {
            result.value = value
            first = false
        }
    }

    return result
}
