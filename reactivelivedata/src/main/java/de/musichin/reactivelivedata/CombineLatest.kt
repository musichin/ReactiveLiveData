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
): LiveData<out R> {
    if (size <= 0) return liveData()

    val result = MediatorLiveData<Any>()

    val values = Array<Any?>(size) { NOT_SET }

    var emits = 0
    var combine = false
    val combineFn: (Int, value: Any?) -> Unit = { index, value ->
        if (!combine) {
            if (values[index] === NOT_SET) {
                emits++
                combine = emits == size
            }
        }
        values[index] = value

        if (combine) {
            result.value = combiner(values as Array<T>)
        }
    }

    var iter = 0
    forEach { source ->
        val index = iter++
        result.addSource(source as LiveData<Any?>) { value ->
            combineFn(index, value)
        }
    }
    return result as LiveData<out R>
}

@MainThread
@CheckResult
private fun <T, R> combineLatest(
    combiner: (Array<T>) -> R,
    vararg sources: LiveData<out T>,
): LiveData<out R> =
    sources.iterator().combineLatest(sources.size, combiner)

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> Array<out LiveData<out T>>.combineLatest(combiner: (List<T>) -> R): LiveData<out R> =
    iterator().combineLatest(size) { values -> combiner(values.toList()) }

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> Iterable<LiveData<out T>>.combineLatest(combiner: (List<T>) -> R): LiveData<out R> {
    val collection = (this as? Collection) ?: toList()

    return collection.iterator()
        .combineLatest(collection.size) { values -> combiner(values.toList()) }
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source to a [Pair].
 * @param source0 first source to combine with `source1`.
 * @param source1 second source to combine with `source0`.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
): LiveData<out Pair<T0, T1>> {
    return combineLatest(source0, source1) { T0, T1 -> T0 to T1 }
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source to a [Triple].
 * @param source0 first source to combine with `source1` and `source2`.
 * @param source1 second source to combine with `source0` and `source2`.
 * @param source2 third source to combine with `source0` and `source1`.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1, T2> combineLatest(
    source0: LiveData<out T0>,
    source1: LiveData<out T1>,
    source2: LiveData<out T2>,
): LiveData<out Triple<T0, T1, T2>> {
    return combineLatest(source0, source1, source2) { T0, T1, T2 -> Triple(T0, T1, T2) }
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
 * @param other second source to combine with current source.
 * @param combiner transformation function for source elements to combine.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1, R> LiveData<out T0>.combineLatestWith(
    other: LiveData<out T1>,
    combiner: (T0, T1) -> R,
): LiveData<out R> =
    combineLatest(this, other, combiner)

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source to a [Pair].
 * @param other second source to combine with current source.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T0, T1> LiveData<out T0>.combineLatestWith(other: LiveData<out T1>): LiveData<out Pair<T0, T1>> =
    combineLatest(this, other) { T0, T1 -> T0 to T1 }

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
 * @param source0 first source to combine with `source1`.
 * @param source1 second source to combine with `source0`.
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
): LiveData<out R> {
    val func = { input: Array<Any> -> combiner(input[0] as T0, input[1] as T1) }

    return combineLatest(func, source0 as LiveData<Any>, source1 as LiveData<Any>)
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
 * @param source0 first source to combine with `source1` and `source2`.
 * @param source1 second source to combine with `source0` and `source2`.
 * @param source2 third source to combine with `source0` and `source1`.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
        combiner(input[0] as T0, input[1] as T1, input[2] as T2)
    }
    return combineLatest(
        func,
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
        combiner(input[0] as T0, input[1] as T1, input[2] as T2, input[3] as T3)
    }
    return combineLatest(
        func,
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
        combiner(input[0] as T0, input[1] as T1, input[2] as T2, input[3] as T3, input[4] as T4)
    }
    return combineLatest(
        func,
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
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
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
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
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
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
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
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
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>,
        source8 as LiveData<Any>,
    )
}

/**
 * Returns new [LiveData] instance that combines latest emitted elements of each source by calling
 * the provided `combiner` function.
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
): LiveData<out R> {
    val func = { input: Array<Any> ->
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
        source0 as LiveData<Any>,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>,
        source8 as LiveData<Any>,
        source9 as LiveData<Any>,
    )
}
