package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

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

fun <T> LiveData<T>.skip(count: Int): LiveData<T> {
    var counter = 0
    return skipWhile { ++counter > count }
}
