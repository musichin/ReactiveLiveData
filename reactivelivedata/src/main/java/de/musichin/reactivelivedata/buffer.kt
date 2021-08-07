package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlin.math.min

/**
 * Returns new [LiveData] instance that emits buffer of elements emitted by given [LiveData].
 * @param count buffer size.
 * @param skip elements to skip before starting a new buffer.
 * @return new [LiveData] instance.
 */
@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.buffer(count: Int, skip: Int): LiveData<List<T>> {
    val buffer: Array<Any?> = arrayOfNulls(min(count, skip))
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

/**
 * Returns new [LiveData] instance that emits buffer of elements emitted by given [LiveData].
 * @param count buffer size.
 * @return new [LiveData] instance.
 */
fun <T> LiveData<T>.buffer(count: Int): LiveData<List<T>> =
    buffer(count, count)
