package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@MainThread
@CheckResult
private fun <T> LiveData<out T>.mergeWith(others: Iterator<LiveData<out T>>): LiveData<T> {
    val sources = linkedSetOf<LiveData<out T>>(this)
    others.forEach { source -> sources.add(source) }

    val result = MediatorLiveData<T>()
    sources.forEach { source ->
        result.addSource(source, result::setValue)
    }

    return result
}

@MainThread
@CheckResult
private fun <T> Iterator<LiveData<out T>>.mergeWith(): LiveData<T> {
    if (!hasNext()) return emptyLiveData()
    return next().mergeWith(this)
}

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * Without sources, it never emits.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> Iterable<LiveData<out T>>.merge(): LiveData<T> = iterator().mergeWith()

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * Without sources, it never emits.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> Array<out LiveData<out T>>.merge(): LiveData<T> = iterator().mergeWith()

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * Without sources, it never emits.
 * @param sources collection of sources to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> mergeArray(vararg sources: LiveData<out T>): LiveData<T> = sources.iterator().mergeWith()

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
): LiveData<T> = mergeArray(source0, source1)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
): LiveData<T> = mergeArray(source0, source1, source2)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> merge(
    source0: LiveData<out T>,
    source1: LiveData<out T>,
    source2: LiveData<out T>,
    source3: LiveData<out T>,
): LiveData<T> = mergeArray(source0, source1, source2, source3)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @param source4 fifth source to merge.
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
): LiveData<T> = mergeArray(source0, source1, source2, source3, source4)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @param source4 fifth source to merge.
 * @param source5 sixth source to merge.
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
): LiveData<T> = mergeArray(source0, source1, source2, source3, source4, source5)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @param source4 fifth source to merge.
 * @param source5 sixth source to merge.
 * @param source6 seventh source to merge.
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
): LiveData<T> = mergeArray(source0, source1, source2, source3, source4, source5, source6)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @param source4 fifth source to merge.
 * @param source5 sixth source to merge.
 * @param source6 seventh source to merge.
 * @param source7 eighth source to merge.
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
): LiveData<T> = mergeArray(source0, source1, source2, source3, source4, source5, source6, source7)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @param source4 fifth source to merge.
 * @param source5 sixth source to merge.
 * @param source6 seventh source to merge.
 * @param source7 eighth source to merge.
 * @param source8 ninth source to merge.
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
): LiveData<T> = mergeArray(source0, source1, source2, source3, source4, source5, source6, source7, source8)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any given source.
 * @param source0 first source to merge.
 * @param source1 second source to merge.
 * @param source2 third source to merge.
 * @param source3 fourth source to merge.
 * @param source4 fifth source to merge.
 * @param source5 sixth source to merge.
 * @param source6 seventh source to merge.
 * @param source7 eighth source to merge.
 * @param source8 ninth source to merge.
 * @param source9 tenth source to merge.
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
): LiveData<T> = mergeArray(source0, source1, source2, source3, source4, source5, source6, source7, source8, source9)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by current source or [other].
 * @param other another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<out T>.mergeWith(other: LiveData<out T>): LiveData<T> = mergeArray(this, other)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by current source or [other].
 * @param other another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> LiveData<out T>.plus(other: LiveData<out T>): LiveData<T> = mergeWith(other)

/**
 * Returns new [LiveData] instance that emits elements that are emitted by current source or any of [others].
 * @param others other sources to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> LiveData<out T>.plus(others: Iterable<LiveData<out T>>): LiveData<T> = mergeWith(others.iterator())

/**
 * Returns new [LiveData] instance that emits elements that are emitted by current source or any of [others].
 * @param others other sources to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> LiveData<out T>.plus(others: Array<out LiveData<out T>>): LiveData<T> = mergeWith(others.iterator())

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any of these sources or [other].
 * @param other another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> Iterable<LiveData<out T>>.plus(other: LiveData<out T>): LiveData<T> = other.mergeWith(iterator())

/**
 * Returns new [LiveData] instance that emits elements that are emitted by any of these sources or [other].
 * @param other another source to merge with.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
operator fun <T> Array<out LiveData<out T>>.plus(other: LiveData<out T>): LiveData<T> = other.mergeWith(iterator())
