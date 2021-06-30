package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.take(count: Int): LiveData<T> {
    if (count <= 0) return liveData()
    var counter = 0
    return takeUntil { ++counter >= count }
}

fun <T> LiveData<T>.takeUntil(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value as T)) result.removeSource(this)
        result.value = value
    }
    return result
}

fun <T> LiveData<T>.takeWhile(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value as T)) result.value = value
        else result.removeSource(this)
    }
    return result
}

fun <T> LiveData<T>.first(): LiveData<T> =
    take(1)
