package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, K> LiveData<T>.distinct(func: (T) -> K): LiveData<T> {
    val keys = hashSetOf<Any?>()
    return filter { keys.add(func(it)) }
}

fun <T> LiveData<T>.distinct(): LiveData<T> =
    distinct { value -> value }

fun <T, K> LiveData<T>.distinctUntilChanged(func: (T) -> K): LiveData<T> {
    var prev: Any? = NOT_SET
    return filter {
        val key = func(it)
        if (key !== prev) {
            prev = key
            true
        } else {
            false
        }
    }
}

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
    Transformations.distinctUntilChanged(this)
