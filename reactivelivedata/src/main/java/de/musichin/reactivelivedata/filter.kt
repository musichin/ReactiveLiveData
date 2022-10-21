package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that filters emitted elements matching given `predicate`.
 * @param predicate element filter.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> if (predicate(value)) result.value = value }
    return result
}

/**
 * Returns new [LiveData] instance that filters emitted elements not matching given `predicate`.
 * @param predicate element filter.
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T> LiveData<T>.filterNot(predicate: (T) -> Boolean): LiveData<T> =
    filter { value -> !predicate(value) }

/**
 * Returns new [LiveData] instance that filters not `null` elements.
 * @return new [LiveData] instance.
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
    filter { it != null } as LiveData<T>

/**
 * Returns new [LiveData] instance that filters elements of given class.
 * @param clazz emits only elements that are instances of it.
 * @return new [LiveData] instance.
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T> LiveData<*>.filterIsInstance(clazz: Class<T>): LiveData<T> =
    filter { clazz.isInstance(it) } as LiveData<T>

/**
 * Returns new [LiveData] instance that filters elements of given class.
 * @return new [LiveData] instance.
 */
@CheckResult
inline fun <reified T> LiveData<*>.filterIsInstance(): LiveData<T> =
    filterIsInstance(T::class.java)
