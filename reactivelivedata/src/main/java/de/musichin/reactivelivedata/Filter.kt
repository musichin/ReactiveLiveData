package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that emits only elements for which [predicate] returns `true`.
 * @param predicate element filter.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.filter(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> if (predicate(value)) result.value = value }
    return result
}

/**
 * Returns new [LiveData] instance that emits only elements for which [predicate] returns `false`.
 * @param predicate element filter.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.filterNot(predicate: (T) -> Boolean): LiveData<T> = filter { value -> !predicate(value) }

/**
 * Returns new [LiveData] instance that emits only elements that are not `null`.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<T?>.filterNotNull(): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> if (value != null) result.value = value }
    return result
}

/**
 * Returns new [LiveData] instance that emits only the non-`null` results of applying [transform]
 * to each element.
 * @param transform function to apply to each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R : Any> LiveData<T>.mapNotNull(transform: (T) -> R?): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) { value -> transform(value)?.let { result.value = it } }
    return result
}

/**
 * Returns new [LiveData] instance that emits only elements that are instances of given class.
 * @param clazz class of elements to emit.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T> LiveData<*>.filterIsInstance(clazz: Class<T>): LiveData<T> {
    val objectType = (clazz as Class<*>).kotlin.javaObjectType
    return filter { objectType.isInstance(it) } as LiveData<T>
}

/**
 * Returns new [LiveData] instance that emits only elements that are instances of given class.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
inline fun <reified T> LiveData<*>.filterIsInstance(): LiveData<T> = filterIsInstance(T::class.java)
