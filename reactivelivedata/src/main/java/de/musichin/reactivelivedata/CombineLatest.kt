package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
private fun <T, R> Iterator<LiveData<out T>>.combineLatest(
    size: Int,
    combiner: (Array<T>) -> R,
): LiveData<R> {
    if (size <= 0) return emptyLiveData()

    val result = MediatorLiveData<R>()

    val values = Array<Any?>(size) { NOT_SET }

    var emits = 0
    var combine = false
    val combineFn: (List<Int>, value: T) -> Unit = { indices, value ->
        indices.forEach { index ->
            if (!combine) {
                if (values[index] === NOT_SET) {
                    emits++
                    combine = emits == size
                }
            }
            values[index] = value
        }

        if (combine) {
            result.value = combiner(values as Array<T>)
        }
    }

    val indicesBySource = LinkedHashMap<LiveData<out T>, MutableList<Int>>()
    asSequence().forEachIndexed { index, source ->
        indicesBySource.getOrPut(source) { mutableListOf() }.add(index)
    }
    indicesBySource.forEach { (source, indices) ->
        result.addSource(source) { value ->
            combineFn(indices, value)
        }
    }
    return result
}

@MainThread
@CheckResult
private fun <T, R> combineLatest(
    combiner: (Array<T>) -> R,
    vararg sources: LiveData<out T>,
): LiveData<R> = sources.iterator().combineLatest(sources.size, combiner)

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * Without sources, it never emits.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> Array<out LiveData<out T>>.combineLatest(combiner: (List<T>) -> R): LiveData<R> =
    iterator().combineLatest(size) { values -> combiner(values.toList()) }

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * Without sources, it never emits.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> Iterable<LiveData<out T>>.combineLatest(combiner: (List<T>) -> R): LiveData<R> {
    val collection = (this as? Collection) ?: toList()

    return collection
        .iterator()
        .combineLatest(collection.size) { values -> combiner(values.toList()) }
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source to a [Pair].
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
): LiveData<Pair<T0, T1>> = combineLatest(source0, source1) { T0, T1 -> T0 to T1 }

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source to a [Triple].
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1, T2> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
): LiveData<Triple<T0, T1, T2>> = combineLatest(source0, source1, source2) { T0, T1, T2 -> Triple(T0, T1, T2) }

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param other second source to combine with current source.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1, R> LiveData<out T0>.combineLatestWith(
    other: LiveData<out T1>,
    combiner: (T0, T1) -> R,
): LiveData<R> = combineLatest(this, other, combiner)

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source to a [Pair].
 * Emits only after every source has emitted at least once.
 * @param other second source to combine with current source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1> LiveData<out T0>.combineLatestWith(other: LiveData<out T1>): LiveData<Pair<T0, T1>> =
    combineLatest(this, other) { T0, T1 -> T0 to T1 }

/**
 * Returns new [LiveData] instance that combines each element of current source with the latest
 * element of [other] by calling the provided [combiner] function.
 * Emits only when current source emits; elements emitted before [other] has emitted are dropped.
 * If [other] already holds a value when the returned [LiveData] becomes active, an element already held by
 * current source is combined with it right away.
 * @param other source whose latest element is combined with each element of current source.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, R> LiveData<out T0>.withLatestFrom(
    other: LiveData<out T1>,
    combiner: (T0, T1) -> R,
): LiveData<R> {
    val result = MediatorLiveData<R>()
    if (other === this) {
        result.addSource(this) { value -> result.value = combiner(value as T0, value as T1) }
        return result
    }

    var latest: Any? = NOT_SET
    result.addSource(other) { value -> latest = value }
    result.addSource(this) { value ->
        if (latest !== NOT_SET) result.value = combiner(value, latest as T1)
    }
    return result
}

/**
 * Returns new [LiveData] instance that combines each element of current source with the latest
 * element of [other] to a [Pair].
 * Emits only when current source emits; elements emitted before [other] has emitted are dropped.
 * If [other] already holds a value when the returned [LiveData] becomes active, an element already held by
 * current source is combined with it right away.
 * @param other source whose latest element is combined with each element of current source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1> LiveData<out T0>.withLatestFrom(other: LiveData<out T1>): LiveData<Pair<T0, T1>> = withLatestFrom(other) { T0, T1 -> T0 to T1 }

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    combiner: (T0, T1) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> -> combiner(input[0] as T0, input[1] as T1) }

    return combineLatest(func, source0, source1)
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    combiner: (T0, T1, T2) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(input[0] as T0, input[1] as T1, input[2] as T2)
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    combiner: (T0, T1, T2, T3) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(input[0] as T0, input[1] as T1, input[2] as T2, input[3] as T3)
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param source4 fifth source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    source4: LiveData<out T4>,
    combiner: (T0, T1, T2, T3, T4) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(input[0] as T0, input[1] as T1, input[2] as T2, input[3] as T3, input[4] as T4)
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
        source4,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param source4 fifth source to combine.
 * @param source5 sixth source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    source4: LiveData<out T4>,
    source5: LiveData<out T5>,
    combiner: (T0, T1, T2, T3, T4, T5) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(
            input[0] as T0,
            input[1] as T1,
            input[2] as T2,
            input[3] as T3,
            input[4] as T4,
            input[5] as T5,
        )
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
        source4,
        source5,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param source4 fifth source to combine.
 * @param source5 sixth source to combine.
 * @param source6 seventh source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, T6, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    source4: LiveData<out T4>,
    source5: LiveData<out T5>,
    source6: LiveData<out T6>,
    combiner: (T0, T1, T2, T3, T4, T5, T6) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(
            input[0] as T0,
            input[1] as T1,
            input[2] as T2,
            input[3] as T3,
            input[4] as T4,
            input[5] as T5,
            input[6] as T6,
        )
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
        source4,
        source5,
        source6,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param source4 fifth source to combine.
 * @param source5 sixth source to combine.
 * @param source6 seventh source to combine.
 * @param source7 eighth source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, T6, T7, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    source4: LiveData<out T4>,
    source5: LiveData<out T5>,
    source6: LiveData<out T6>,
    source7: LiveData<out T7>,
    combiner: (T0, T1, T2, T3, T4, T5, T6, T7) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(
            input[0] as T0,
            input[1] as T1,
            input[2] as T2,
            input[3] as T3,
            input[4] as T4,
            input[5] as T5,
            input[6] as T6,
            input[7] as T7,
        )
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
        source4,
        source5,
        source6,
        source7,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param source4 fifth source to combine.
 * @param source5 sixth source to combine.
 * @param source6 seventh source to combine.
 * @param source7 eighth source to combine.
 * @param source8 ninth source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    source4: LiveData<out T4>,
    source5: LiveData<out T5>,
    source6: LiveData<out T6>,
    source7: LiveData<out T7>,
    source8: LiveData<out T8>,
    combiner: (T0, T1, T2, T3, T4, T5, T6, T7, T8) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(
            input[0] as T0,
            input[1] as T1,
            input[2] as T2,
            input[3] as T3,
            input[4] as T4,
            input[5] as T5,
            input[6] as T6,
            input[7] as T7,
            input[8] as T8,
        )
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
        source4,
        source5,
        source6,
        source7,
        source8,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided [combiner] function.
 * Emits only after every source has emitted at least once.
 * @param source0 first source to combine.
 * @param source1 second source to combine.
 * @param source2 third source to combine.
 * @param source3 fourth source to combine.
 * @param source4 fifth source to combine.
 * @param source5 sixth source to combine.
 * @param source6 seventh source to combine.
 * @param source7 eighth source to combine.
 * @param source8 ninth source to combine.
 * @param source9 tenth source to combine.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
    source3: LiveData<out T3>,
    source4: LiveData<out T4>,
    source5: LiveData<out T5>,
    source6: LiveData<out T6>,
    source7: LiveData<out T7>,
    source8: LiveData<out T8>,
    source9: LiveData<out T9>,
    combiner: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R,
): LiveData<R> {
    val func = { input: Array<Any?> ->
        combiner(
            input[0] as T0,
            input[1] as T1,
            input[2] as T2,
            input[3] as T3,
            input[4] as T4,
            input[5] as T5,
            input[6] as T6,
            input[7] as T7,
            input[8] as T8,
            input[9] as T9,
        )
    }
    return combineLatest(
        func,
        source0,
        source1,
        source2,
        source3,
        source4,
        source5,
        source6,
        source7,
        source8,
        source9,
    )
}
