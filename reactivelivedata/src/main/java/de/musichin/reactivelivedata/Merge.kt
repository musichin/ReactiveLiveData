package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@MainThread
@CheckResult
private fun <T> LiveData<out T>.mergeWith(others: Iterator<LiveData<out T>>): LiveData<out T> {
    if (!others.hasNext()) return this

    val result = MediatorLiveData<T>()
    result.addSource(this, result::setValue)
    others.forEach { source ->
        result.addSource(source, result::setValue)
    }

    return result
}

@MainThread
@CheckResult
private fun <T> Iterator<LiveData<out T>>.mergeWith(): LiveData<out T> {
    if (!hasNext()) return liveData()
    return next().mergeWith(this)
}

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> Iterable<LiveData<out T>>.merge(): LiveData<out T> =
    iterator().mergeWith()

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> Array<out LiveData<out T>>.merge(): LiveData<out T> =
    iterator().mergeWith()

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param sources collection of source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> mergeArray(vararg sources: LiveData<out T>): LiveData<out T> =
    sources.iterator().mergeWith()

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(source0: LiveData<out T>, source1: LiveData<out T>): LiveData<out T> =
    mergeArray(source0, source1)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(source0: LiveData<out T>, source1: LiveData<out T>, source2: LiveData<out T>): LiveData<out T> =
    mergeArray(source0, source1, source2)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
    source4: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3, source4)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
    source4: LiveData<out T>,
    source5: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3, source4, source5)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
    source4: LiveData<out T>,
    source5: LiveData<out T>,
    source6: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3, source4, source5, source6)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
    source4: LiveData<out T>,
    source5: LiveData<out T>,
    source6: LiveData<out T>,
    source7: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3, source4, source5, source6, source7)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
    source4: LiveData<out T>,
    source5: LiveData<out T>,
    source6: LiveData<out T>,
    source7: LiveData<out T>,
    source8: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3, source4, source5, source6, source7, source8)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
    source4: LiveData<out T>,
    source5: LiveData<out T>,
    source6: LiveData<out T>,
    source7: LiveData<out T>,
    source8: LiveData<out T>,
    source9: LiveData<out T>,
): LiveData<out T> =
    mergeArray(source0, source1, source2, source3, source4, source5, source6, source7, source8, source9)

/**
 * Returns new [LiveData] instance that emits elements that are either emitted by this or any given source.
 * @param other another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<out T>.mergeWith(other: LiveData<out T>): LiveData<out T> =
    mergeArray(this, other)

/**
 * Returns new [LiveData] instance that emits elements that are either emitted by this or any given source.
 * @param other another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> LiveData<out T>.plus(other: LiveData<out T>): LiveData<out T> =
    mergeWith(other)

/**
 * Returns new [LiveData] instance that emits elements that are either emitted by this or any given source.
 * @param others another sources to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> LiveData<out T>.plus(others: Iterable<LiveData<out T>>): LiveData<out T> =
    mergeWith(others.iterator())

/**
 * Returns new [LiveData] instance that emits elements that are either emitted by this or any given source.
 * @param others another sources to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> LiveData<out T>.plus(others: Array<out LiveData<out T>>): LiveData<out T> =
    mergeWith(others.iterator())

/**
 * Returns new [LiveData] instance that emits elements that are either emitted by these or given source.
 * @param other  another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> Iterable<LiveData<out T>>.plus(other: LiveData<out T>): LiveData<out T> =
    other.mergeWith(iterator())

/**
 * Returns new [LiveData] instance that emits elements that are either emitted by these or given source.
 * @param other  another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> Array<out LiveData<out T>>.plus(other: LiveData<out T>): LiveData<out T> =
    other.mergeWith(iterator())
