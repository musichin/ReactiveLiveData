package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@Suppress("UNCHECKED_CAST")
private fun <T, R> Iterator<LiveData<out T>>.combineLatest(
    size: Int,
    combiner: (Array<T>) -> R
): LiveData<R> {
    if (size <= 0) return liveData()

    val result = MediatorLiveData<Any>()

    val values = arrayOfNulls<Any?>(size)
    for (index in 0 until size) values[index] = NOT_SET

    var emits = 0
    val combineFn: (Int, value: Any?) -> Unit = { index, value ->
        var combine = emits == size
        if (!combine) {
            if (values[index] === NOT_SET) {
                emits++
            }
            combine = emits == size
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
    return result as LiveData<R>
}

private fun <T, R> combineLatest(
    combiner: (Array<T>) -> R,
    vararg sources: LiveData<T>
): LiveData<R> =
    sources.iterator().combineLatest(sources.size, combiner)

fun <T, R> Array<LiveData<T>>.combineLatest(combiner: (List<T>) -> R): LiveData<R> =
    iterator().combineLatest(size) { values -> combiner(values.toList()) }

fun <T, R> Iterable<LiveData<out T>>.combineLatest(combiner: (List<T>) -> R): LiveData<R> =
    toList().combineLatest(combiner)

fun <T, R> Collection<LiveData<out T>>.combineLatest(combiner: (List<T>) -> R): LiveData<R> =
    iterator().combineLatest(size) { values -> combiner(values.toList()) }

fun <T1, T2> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>
): LiveData<Pair<T1, T2>> {
    return combineLatest(source1, source2) { t1, t2 -> t1 to t2 }
}

fun <T1, T2, T3> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>
): LiveData<Triple<T1, T2, T3>> {
    return combineLatest(source1, source2, source3) { t1, t2, t3 -> Triple(t1, t2, t3) }
}

fun <T1, T2, R> LiveData<T1>.combineLatestWith(
    other: LiveData<T2>,
    combiner: (T1, T2) -> R
): LiveData<R> =
    combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(other: LiveData<T2>): LiveData<Pair<T1, T2>> =
    combineLatest(this, other) { t1, t2 -> t1 to t2 }

@Suppress("UNCHECKED_CAST")
fun <T1, T2, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    combiner: (T1, T2) -> R
): LiveData<R> {
    val func = { input: Array<Any> -> combiner(input[0] as T1, input[1] as T2) }

    return combineLatest(func, source1 as LiveData<Any>, source2 as LiveData<Any>)
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    combiner: (T1, T2, T3) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(input[0] as T1, input[1] as T2, input[2] as T3)
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    combiner: (T1, T2, T3, T4) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4)
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    combiner: (T1, T2, T3, T4, T5) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4, input[4] as T5)
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, T6, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    source6: LiveData<T6>,
    combiner: (T1, T2, T3, T4, T5, T6) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(
            input[0] as T1,
            input[1] as T2,
            input[2] as T3,
            input[3] as T4,
            input[4] as T5,
            input[5] as T6
        )
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    source6: LiveData<T6>,
    source7: LiveData<T7>,
    combiner: (T1, T2, T3, T4, T5, T6, T7) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(
            input[0] as T1,
            input[1] as T2,
            input[2] as T3,
            input[3] as T4,
            input[4] as T5,
            input[5] as T6,
            input[6] as T7
        )
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    source6: LiveData<T6>,
    source7: LiveData<T7>,
    source8: LiveData<T8>,
    combiner: (T1, T2, T3, T4, T5, T6, T7, T8) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(
            input[0] as T1,
            input[1] as T2,
            input[2] as T3,
            input[3] as T4,
            input[4] as T5,
            input[5] as T6,
            input[6] as T7,
            input[7] as T8
        )
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>,
        source8 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    source6: LiveData<T6>,
    source7: LiveData<T7>,
    source8: LiveData<T8>,
    source9: LiveData<T9>,
    combiner: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(
            input[0] as T1,
            input[1] as T2,
            input[2] as T3,
            input[3] as T4,
            input[4] as T5,
            input[5] as T6,
            input[6] as T7,
            input[7] as T8,
            input[8] as T9
        )
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>,
        source8 as LiveData<Any>,
        source9 as LiveData<Any>
    )
}

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> combineLatest(
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    source3: LiveData<T3>,
    source4: LiveData<T4>,
    source5: LiveData<T5>,
    source6: LiveData<T6>,
    source7: LiveData<T7>,
    source8: LiveData<T8>,
    source9: LiveData<T9>,
    source10: LiveData<T10>,
    combiner: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R
): LiveData<R> {
    val func = { input: Array<Any> ->
        combiner(
            input[0] as T1,
            input[1] as T2,
            input[2] as T3,
            input[3] as T4,
            input[4] as T5,
            input[5] as T6,
            input[6] as T7,
            input[7] as T8,
            input[8] as T9,
            input[9] as T10
        )
    }
    return combineLatest(
        func,
        source1 as LiveData<Any>,
        source2 as LiveData<Any>,
        source3 as LiveData<Any>,
        source4 as LiveData<Any>,
        source5 as LiveData<Any>,
        source6 as LiveData<Any>,
        source7 as LiveData<Any>,
        source8 as LiveData<Any>,
        source9 as LiveData<Any>,
        source10 as LiveData<Any>
    )
}
