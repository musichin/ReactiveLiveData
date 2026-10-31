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
fun <T> LiveData<T>.skipUntil(predicate: (T) -> Boolean): LiveData<T> {
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
 * Returns new [LiveData] instance that skips first elements as long as [predicate] returns `true`.
 * @param predicate function to test each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.skipWhile(predicate: (T) -> Boolean): LiveData<T> = skipUntil { !predicate(it) }

/**
 * Returns new [LiveData] instance that skips first [count] elements.
 * @param count number of elements to skip, must not be negative.
 * @return new [LiveData] instance.
 * @throws IllegalArgumentException if [count] is negative.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    require(count >= 0) { "count $count must not be negative." }
    var counter = 0
    return skipUntil { ++counter > count }
}
