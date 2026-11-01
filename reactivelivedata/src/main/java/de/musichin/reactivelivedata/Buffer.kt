package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Returns new [LiveData] instance that emits buffer of elements emitted by given [LiveData].
 * A new buffer starts every [skip] elements and is emitted as soon as it holds [count] elements.
 * If [skip] is less than [count], buffers overlap; if it is greater, elements in between are dropped.
 * A partially filled buffer is never emitted.
 * @param count buffer size, must be greater than zero.
 * @param skip elements to skip before starting a new buffer, must be greater than zero.
 * @return new [LiveData] instance.
 * @throws IllegalArgumentException if [count] or [skip] is not greater than zero.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.buffer(
    count: Int,
    skip: Int,
): LiveData<List<T>> {
    require(count > 0) { "count $count must be greater than zero." }
    require(skip > 0) { "skip $skip must be greater than zero." }

    val buffers = ArrayDeque<MutableList<T>>()
    val result = MediatorLiveData<List<T>>()
    var counter = 0
    result.addSource(this) { value ->
        if (counter == 0) buffers.addLast(ArrayList(count))
        counter = (counter + 1) % skip

        buffers.forEach { buffer -> buffer.add(value) }
        if (buffers.firstOrNull()?.size == count) {
            result.value = buffers.removeFirst()
        }
    }
    return result
}

/**
 * Returns new [LiveData] instance that emits buffer of elements emitted by given [LiveData].
 * @param count buffer size, must be greater than zero.
 * @return new [LiveData] instance.
 * @throws IllegalArgumentException if [count] is not greater than zero.
 */
@MainThread
@CheckResult
fun <T> LiveData<T>.buffer(count: Int): LiveData<List<T>> = buffer(count, count)
