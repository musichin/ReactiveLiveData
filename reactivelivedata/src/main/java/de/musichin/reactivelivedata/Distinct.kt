package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations

/**
 * Returns new [LiveData] instance that emits only distinct elements
 * using given key selector result for comparison.
 * @param keySelector is applied to each element to determine the key.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T, K> LiveData<T>.distinct(keySelector: (T) -> K): LiveData<T> {
    val keys = hashSetOf<Any?>()
    return filter { keys.add(keySelector(it)) }
}

/**
 * Returns new [LiveData] instance that emits only distinct elements using [Any.equals] for comparison.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.distinct(): LiveData<T> =
    distinct { value -> value }

/**
 * Returns new [LiveData] instance that ensures emitted element not equals to previous element
 * using given key selector result for comparison.
 * @param keySelector is applied to each element to determine the key.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T, K> LiveData<T>.distinctUntilChanged(keySelector: (T) -> K): LiveData<T> {
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
 * using `comparer` for comparison.
 * @param comparer function to use for equality comparison.
 * @return new [LiveData] instance.
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.distinctUntilChanged(comparer: (T, T) -> Boolean): LiveData<T> {
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

/**
 * Returns new [LiveData] instance that ensures emitted element is not equal to previous element
 * using [Any.equals] for comparison.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
    Transformations.distinctUntilChanged(this)
