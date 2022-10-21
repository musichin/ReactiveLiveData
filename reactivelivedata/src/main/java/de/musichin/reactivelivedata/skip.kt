package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that skips first elements until `predicate` returns `true`.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.skipWhile(predicate: (T) -> Boolean): LiveData<T> {
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
 * Returns new [LiveData] instance that skips first `count` elements.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    var counter = 0
    return skipWhile { ++counter > count }
}
