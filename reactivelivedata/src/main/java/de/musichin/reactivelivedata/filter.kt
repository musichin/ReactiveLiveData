package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> if (func(value as T)) result.value = value }
    return result
}

fun <T> LiveData<T>.filterNot(func: (T) -> Boolean): LiveData<T> =
    filter { value -> !func(value) }

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
    filter { it != null } as LiveData<T>

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<*>.filterIsInstance(clazz: Class<T>): LiveData<T> =
    filter { clazz.isInstance(it) } as LiveData<T>

inline fun <reified T> LiveData<*>.filterIsInstance(): LiveData<T> =
    filterIsInstance(T::class.java)
