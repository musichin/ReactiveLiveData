package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@MainThread
@CheckResult
private fun <T> Iterator<LiveData<out T>>.amb(): LiveData<T> {
    val sources = linkedSetOf<LiveData<out T>>()
    forEach { source -> sources.add(source) }
    if (sources.isEmpty()) return emptyLiveData()

    val result = MediatorLiveData<T>()
    sources.forEach { source ->
        result.addSource(source) { value ->
            sources.forEach { other -> if (other !== source) result.removeSource(other) }
            sources.clear()
            result.value = value
        }
    }
    return result
}

/**
 * Returns new [LiveData] instance that emits elements of the source that emits first.
 * If several sources already hold a value when the returned [LiveData] becomes active,
 * the first of them in iteration order wins.
 * Without sources, it never emits.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> Iterable<LiveData<out T>>.amb(): LiveData<T> = iterator().amb()

/**
 * Returns new [LiveData] instance that emits elements of the source that emits first.
 * If several sources already hold a value when the returned [LiveData] becomes active,
 * the first of them in iteration order wins.
 * Without sources, it never emits.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> Array<out LiveData<out T>>.amb(): LiveData<T> = iterator().amb()

/**
 * Returns new [LiveData] instance that emits elements of current source or [other],
 * whichever emits first.
 * If both already hold a value when the returned [LiveData] becomes active, current source wins.
 * @param other source to compete with current source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<out T>.ambWith(other: LiveData<out T>): LiveData<T> = arrayOf(this, other).amb()
