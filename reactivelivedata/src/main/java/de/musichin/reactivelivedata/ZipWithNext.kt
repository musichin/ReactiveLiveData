package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that emits the result of applying [transform] to each element
 * and the element before it.
 * The first element is not emitted, since it has no predecessor.
 * Only elements received while the returned [LiveData] is active are paired.
 * @param transform function that combines the previous element with the current one.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T, R> LiveData<T>.zipWithNext(transform: (previous: T, current: T) -> R): LiveData<R> {
    val result = MediatorLiveData<R>()
    var previous: Any? = NOT_SET
    result.addSource(this) { value ->
        if (previous !== NOT_SET) result.value = transform(previous as T, value)
        previous = value
    }
    return result
}

/**
 * Returns new [LiveData] instance that emits each element paired with the element before it.
 * The first element is not emitted, since it has no predecessor.
 * Only elements received while the returned [LiveData] is active are paired.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.zipWithNext(): LiveData<Pair<T, T>> = zipWithNext { previous, current -> previous to current }
