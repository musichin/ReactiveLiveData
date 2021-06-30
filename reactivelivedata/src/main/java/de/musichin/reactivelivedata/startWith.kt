package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.startWith(value: T): LiveData<T> =
    startWith(liveData(value))

fun <T> LiveData<T>.startWith(value: () -> T): LiveData<T> =
    startWith(liveData(value))

fun <T> LiveData<T>.startWith(value: LiveData<T>): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(value) { valueStart ->
        result.removeSource(value)
        result.value = valueStart
        result.addSource(this) { valueMain ->
            result.value = valueMain
        }
    }
    return result
}
