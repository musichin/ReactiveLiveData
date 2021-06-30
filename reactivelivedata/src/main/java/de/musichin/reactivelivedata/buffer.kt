package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.buffer(count: Int, skip: Int): LiveData<List<T>> {
    val buffer: Array<Any?> = arrayOfNulls(if (count > skip) skip else count)
    val result = MediatorLiveData<List<T>>()
    var counter = 0
    result.addSource(this) {
        if (counter < count) {
            buffer[counter] = it
        }
        counter++
        if (counter == skip) {
            counter = 0
            result.value = buffer.toList() as List<T>
        }
    }
    return result
}

fun <T> LiveData<T>.buffer(count: Int): LiveData<List<T>> =
    buffer(count, count)
