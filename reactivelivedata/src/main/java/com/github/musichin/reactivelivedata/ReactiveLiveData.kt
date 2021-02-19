package com.github.musichin.reactivelivedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

private val NOT_SET = Any()
private val NEVER = object : LiveData<Any?>() {}

object ReactiveLiveData {

    @MainThread
    @Suppress("UNCHECKED_CAST")
    fun <T, R> combineLatest(sources: Array<out LiveData<out T>>, combiner: (Array<T>) -> R): LiveData<R> {
        if (sources.isEmpty()) return liveData()

        val size = sources.size
        val result = MediatorLiveData<Any>()

        val values = arrayOfNulls<Any?>(size)
        for (index in 0 until size) values[index] = NOT_SET

        var emits = 0
        for (index in 0 until size) {
            val observer = { t: Any ->
                var combine = emits == size
                if (!combine) {
                    if (values[index] === NOT_SET) emits++
                    combine = emits == size
                }
                values[index] = t

                if (combine) {
                    result.value = combiner(values as Array<T>)
                }
            }
            result.addSource(sources[index] as LiveData<Any>, observer)
        }
        return result as LiveData<R>
    }

    @MainThread
    @JvmStatic
    fun <T, R> combineLatest(combiner: (Array<T>) -> R, vararg sources: LiveData<T>): LiveData<R> {
        return combineLatest(sources, combiner)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, R> combineLatest(source1: LiveData<T1>,
                                  source2: LiveData<T2>,
                                  combiner: (T1, T2) -> R): LiveData<R> {
        val func = { input: Array<Any> -> combiner(input[0] as T1, input[1] as T2) }

        return combineLatest(func, source1 as LiveData<Any>, source2 as LiveData<Any>)
    }

    @MainThread
    @JvmStatic
    fun <T1, T2> combineLatest(source1: LiveData<T1>, source2: LiveData<T2>): LiveData<Pair<T1, T2>> {
        return combineLatest(source1, source2) { t1, t2 -> t1 to t2 }
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, R> combineLatest(source1: LiveData<T1>,
                                      source2: LiveData<T2>,
                                      source3: LiveData<T3>,
                                      combiner: (T1, T2, T3) -> R): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3)
        }
        return combineLatest(func, source1 as LiveData<Any>, source2 as LiveData<Any>, source3 as LiveData<Any>)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, R> combineLatest(source1: LiveData<T1>,
                                          source2: LiveData<T2>,
                                          source3: LiveData<T3>,
                                          source4: LiveData<T4>,
                                          combiner: (T1, T2, T3, T4) -> R): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4)
        }
        return combineLatest(func,
                source1 as LiveData<Any>,
                source2 as LiveData<Any>,
                source3 as LiveData<Any>,
                source4 as LiveData<Any>)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, R> combineLatest(source1: LiveData<T1>,
                                              source2: LiveData<T2>,
                                              source3: LiveData<T3>,
                                              source4: LiveData<T4>,
                                              source5: LiveData<T5>,
                                              combiner: (T1, T2, T3, T4, T5) -> R): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4, input[4] as T5)
        }
        return combineLatest(func,
                source1 as LiveData<Any>,
                source2 as LiveData<Any>,
                source3 as LiveData<Any>,
                source4 as LiveData<Any>,
                source5 as LiveData<Any>)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, R> combineLatest(
            source1: LiveData<T1>,
            source2: LiveData<T2>,
            source3: LiveData<T3>,
            source4: LiveData<T4>,
            source5: LiveData<T5>,
            source6: LiveData<T6>,
            combiner: (T1, T2, T3, T4, T5, T6) -> R): LiveData<R> {
        val func = { input: Array<Any> ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4, input[4] as T5, input[5] as T6)
        }
        return combineLatest(func,
                source1 as LiveData<Any>,
                source2 as LiveData<Any>,
                source3 as LiveData<Any>,
                source4 as LiveData<Any>,
                source5 as LiveData<Any>,
                source6 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatest(
            source1: LiveData<T1>,
            source2: LiveData<T2>,
            source3: LiveData<T3>,
            source4: LiveData<T4>,
            source5: LiveData<T5>,
            source6: LiveData<T6>,
            source7: LiveData<T7>,
            combiner: (T1, T2, T3, T4, T5, T6, T7) -> R): LiveData<R> {
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
        return combineLatest(func,
                source1 as LiveData<Any>,
                source2 as LiveData<Any>,
                source3 as LiveData<Any>,
                source4 as LiveData<Any>,
                source5 as LiveData<Any>,
                source6 as LiveData<Any>,
                source7 as LiveData<Any>
        )
    }

    @MainThread
    @JvmStatic
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
            combiner: (T1, T2, T3, T4, T5, T6, T7, T8) -> R): LiveData<R> {
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
        return combineLatest(func,
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

    @MainThread
    @JvmStatic
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
            combiner: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R): LiveData<R> {
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
        return combineLatest(func,
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

    @MainThread
    @JvmStatic
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
            combiner: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R): LiveData<R> {
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
        return combineLatest(func,
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

    @MainThread
    @JvmStatic
    fun <T1, T2, T3> combineLatest(source1: LiveData<T1>,
                                   source2: LiveData<T2>,
                                   source3: LiveData<T3>): LiveData<Triple<T1, T2, T3>> {
        return combineLatest(source1, source2, source3) { t1, t2, t3 -> Triple(t1, t2, t3) }
    }

    @MainThread
    @JvmStatic
    fun <T> merge(vararg sources: LiveData<T>): LiveData<T> {
        if (sources.isEmpty()) return liveData()

        val result = MediatorLiveData<T>()

        for (source in sources) {
            result.addSource(source, result::setValue)
        }
        return result
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> liveData(): LiveData<T> = NEVER as LiveData<T>

fun <T> liveData(value: T): LiveData<T> = object : LiveData<T>() {
    init {
        this.value = value
    }
}

fun <T> liveData(value: () -> T): LiveData<T> = object : LiveData<T>() {
    private var initialized = false
    override fun onActive() {
        if (initialized) return
        this.value = value()
        initialized = true
    }
}

// TODO
fun <T> MutableLiveData<T>.hide(): LiveData<T> =
        this

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: MutableLiveData<T>) =
        this.observe(owner, observer::setValue)

fun <T> LiveData<T>.observe(owner: LifecycleOwner) =
        observe(owner, {})

fun <T> LiveData<T>.observeForever() = this.observeForever {}

fun <T> LiveData<T>.observeForever(observer: MutableLiveData<T>) =
        this.observeForever(observer::setValue)

fun <T> T.toLiveData(): LiveData<T> =
        liveData(this)

@Suppress("UNCHECKED_CAST")
fun <T, U> LiveData<T>.cast(clazz: Class<U>): LiveData<U> =
        map { clazz.cast(it) as U }

inline fun <T, reified U> LiveData<T>.cast(): LiveData<U> =
        cast(U::class.java)


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

fun <T> LiveData<T>.take(count: Int): LiveData<T> {
    if (count <= 0) return liveData()
    var counter = 0
    return takeUntil { ++counter >= count }
}

fun <T> LiveData<T>.first(): LiveData<T> =
        take(1)

fun <T> LiveData<T>.takeUntil(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value as T)) result.removeSource(this)
        result.value = value
    }
    return result
}

fun <T> LiveData<T>.takeWhile(predicate: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        if (predicate(value as T)) result.value = value
        else result.removeSource(this)
    }
    return result
}


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

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
        Transformations.map(this, func)

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>?): LiveData<R> =
        Transformations.switchMap(this, func)

fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> =
        switchMap { it }

fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> if (func(value as T)) result.value = value }
    return result
}

fun <T> LiveData<T>.filterNot(func: (T) -> Boolean): LiveData<T> =
        filter { value -> !func(value) }

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
        filter { it != null } as LiveData<T>

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<*>.filterIsInstance(clazz: Class<T>): LiveData<T> =
        filter { clazz.isInstance(it) } as LiveData<T>

inline fun <reified T> LiveData<*>.filterIsInstance(): LiveData<T> =
        filterIsInstance(T::class.java)

fun <T, K> LiveData<T>.distinct(func: (T) -> K): LiveData<T> {
    val keys = hashSetOf<Any?>()
    return filter { keys.add(func(it)) }
}

fun <T> LiveData<T>.distinct(): LiveData<T> =
        distinct { value -> value }

fun <T, K> LiveData<T>.distinctUntilChanged(func: (T) -> K): LiveData<T> {
    var prev: Any? = NOT_SET
    return filter {
        val key = func(it)
        if (key !== prev) {
            prev = key
            true
        } else {
            false
        }
    }
}

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
        Transformations.distinctUntilChanged(this)

fun <T> Array<LiveData<T>>.merge(): LiveData<T> =
        ReactiveLiveData.merge(*this)

operator fun <T> LiveData<T>.plus(other: LiveData<T>): LiveData<T> =
        mergeWith(other)

fun <T> Collection<LiveData<T>>.merge(): LiveData<T> =
        ReactiveLiveData.merge(*this.toTypedArray())

fun <T> LiveData<T>.mergeWith(other: LiveData<T>): LiveData<T> =
        ReactiveLiveData.merge(this, other)

fun <T> LiveData<T>.mergeWith(other: Collection<LiveData<T>>): LiveData<T> =
        ReactiveLiveData.merge(this, *other.toTypedArray())

fun <T> LiveData<T>.mergeWith(other: Array<LiveData<T>>): LiveData<T> =
        ReactiveLiveData.merge(this, *other)

fun <T, R> Array<LiveData<T>>.combineLatest(combiner: (Array<T>) -> R): LiveData<R> =
        ReactiveLiveData.combineLatest(this, combiner)

fun <T, R> Collection<LiveData<out T>>.combineLatest(combiner: (Array<T>) -> R): LiveData<R> =
        ReactiveLiveData.combineLatest(this.toTypedArray(), combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: (T1, T2) -> R): LiveData<R> =
        ReactiveLiveData.combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(other: LiveData<T2>): LiveData<Pair<T1, T2>> =
        ReactiveLiveData.combineLatest(this, other) { t1, t2 -> t1 to t2 }

fun <T> LiveData<T>.doOnValue(func: (T) -> Unit): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) {
        result.value = it
        func(it as T)
    }
    return result
}

fun <T> LiveData<T>.doOnActive(func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onActive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { result.value = it }
    result.addSource(hook) { }
    return result
}

fun <T> LiveData<T>.doOnInactive(func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onInactive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> result.value = value }
    result.addSource(hook) { }
    return result
}
