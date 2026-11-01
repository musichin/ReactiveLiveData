package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that emits the latest element of current source whenever [other] emits.
 * An element is emitted at most once; if current source has not emitted since the last emission,
 * nothing is emitted.
 * If [other] already holds a value when the returned [LiveData] becomes active, an element already held by
 * current source is emitted right away.
 * @param other source whose emissions trigger sampling.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
public fun <T> LiveData<T>.sample(other: LiveData<*>): LiveData<T> {
    val result = MediatorLiveData<T>()
    if (other === this) {
        result.addSource(this) { value -> result.value = value }
        return result
    }

    var latest: Any? = NOT_SET
    result.addSource(this) { value -> latest = value }
    result.addSource(other) {
        if (latest !== NOT_SET) {
            result.value = latest as T
            latest = NOT_SET
        }
    }
    return result
}
