package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that takes only first `count` elements.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.take(count: Int): LiveData<T> {
    if (count <= 0) return liveData()
    var counter = 0
    return takeUntil { ++counter >= count }
}

/**
 * Returns new [LiveData] instance that takes first elements until `predicate` returns `true`.
 * @return new [LiveData] instance.
 */
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
 * Returns new [LiveData] instance that takes first elements as long as `predicate` returns `true`.
 * @return new [LiveData] instance.
 */
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
@CheckResult
fun <T> LiveData<T>.first(): LiveData<T> =
    take(1)
