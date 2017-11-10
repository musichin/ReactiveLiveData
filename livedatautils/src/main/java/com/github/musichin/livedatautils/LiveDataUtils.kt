package com.github.musichin.livedatautils

import android.arch.core.util.Function
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.support.annotation.MainThread
import java.util.HashSet

class LiveDataUtils internal constructor() {
    companion object {
        private val NOT_SET = Any()

        @MainThread
        @JvmStatic
        fun <T> never(): LiveData<T> {
            return object : LiveData<T>() {

            }
        }

        @MainThread
        @JvmStatic
        fun <T> just(value: T): LiveData<T> {
            return object : LiveData<T>() {
                init {
                    setValue(value)
                }
            }
        }

        @MainThread
        @JvmStatic
        fun <T, R> map(source: LiveData<T>, func: Function<T, R>): LiveData<R> {
            return Transformations.map(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T, R> map(source: LiveData<T>, func: (T) -> R): LiveData<R> {
            return Transformations.map(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T, R> switchMap(source: LiveData<T>, func: Function<T, LiveData<R>>): LiveData<R> {
            return Transformations.switchMap(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T, R> switchMap(source: LiveData<T>, func: (T) -> LiveData<R>): LiveData<R> {
            return Transformations.switchMap(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T> filter(source: LiveData<T>, func: Function<T, Boolean>): LiveData<T> {
            return filter(source, func::apply)
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> filter(source: LiveData<T>, func: (T) -> Boolean): LiveData<T> {
            val result = MediatorLiveData<T>()
            result.addSource(source) { t -> if (func(t as T)) result.value = t }
            return result
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> filterNotNull(source: LiveData<T?>): LiveData<T> {
            return filter(source) { it != null } as LiveData<T>
        }

        @MainThread
        @JvmStatic
        fun <T> distinctUntilChanged(source: LiveData<T>, func: Function<T, Any>): LiveData<T> {
            return distinctUntilChanged(source, func::apply)
        }

        @MainThread
        @JvmStatic
        fun <T> distinctUntilChanged(source: LiveData<T>, func: (T) -> Any): LiveData<T> {
            var prev = NOT_SET
            return filter(source) {
                val key = func(it)
                if (key !== prev) {
                    prev = key
                    true
                } else {
                    false
                }
            }
        }

        @MainThread
        @JvmStatic
        fun <T> distinctUntilChanged(source: LiveData<T>): LiveData<T> {
            val func = Function<T, Any> { input -> input }

            return distinctUntilChanged(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T> merge(vararg sources: LiveData<T>): LiveData<T> {
            if (sources.size <= 0) {
                return LiveDataUtils.never()
            }

            val result = MediatorLiveData<T>()

            val observer = Observer<T> { t -> result.value = t }

            for (source in sources) {
                result.addSource(source, observer)
            }
            return result
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T, R> combineLatest(sources: Array<out LiveData<out T>>, combiner: Function<Array<T>, R>): LiveData<R> {
            if (sources.size <= 0) {
                return LiveDataUtils.never()
            }

            val size = sources.size
            val result = MediatorLiveData<Any>()

            val values = arrayOfNulls<Any?>(size)
            for (index in 0..size - 1) values[index] = NOT_SET

            val emits = HashSet<Int>()
            for (index in 0..size - 1) {
                val observer = Observer<Any> { t ->
                    values[index] = t
                    var combine = emits.size == size
                    if (!combine) {
                        emits.add(index)
                        combine = emits.size == size
                    }

                    if (combine) {
                        result.value = combiner.apply(values as Array<T>)
                    }
                }
                result.addSource(sources[index] as LiveData<Any>, observer)
            }
            return result as LiveData<R>
        }

        @MainThread
        @JvmStatic
        fun <T, R> combineLatest(combiner: Function<Array<T>, R>, vararg sources: LiveData<T>): LiveData<R> {
            return combineLatest(sources, combiner)
        }

        @MainThread
        @JvmStatic
        fun <T1, T2, R> combineLatest(source1: LiveData<T1>,
                                      source2: LiveData<T2>,
                                      combiner: Function2<T1, T2, R>): LiveData<R> {
            return combineLatest(source1, source2, combiner::apply)
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T1, T2, R> combineLatest(source1: LiveData<T1>,
                                      source2: LiveData<T2>,
                                      combiner: (T1, T2) -> R): LiveData<R> {
            val func = Function<Array<Any>, R> { input -> combiner(input[0] as T1, input[1] as T2) }

            return combineLatest(func, source1 as LiveData<Any>, source2 as LiveData<Any>)
        }

        @MainThread
        @JvmStatic
        fun <T1, T2> combineLatest(source1: LiveData<T1>, source2: LiveData<T2>): LiveData<Pair<T1, T2>> {
            return combineLatest(source1, source2) { t1, t2 -> t1 to t2 }
        }

        @MainThread
        @JvmStatic
        fun <T1, T2, T3, R> combineLatest(source1: LiveData<T1>,
                                          source2: LiveData<T2>,
                                          source3: LiveData<T3>,
                                          combiner: Function3<T1, T2, T3, R>): LiveData<R> {
            return combineLatest(source1, source2, source3, combiner::apply)
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T1, T2, T3, R> combineLatest(source1: LiveData<T1>,
                                          source2: LiveData<T2>,
                                          source3: LiveData<T3>,
                                          combiner: (T1, T2, T3) -> R): LiveData<R> {
            val func = Function<Array<Any>, R> { input -> combiner(input[0] as T1, input[1] as T2, input[2] as T3) }
            return combineLatest(func, source1 as LiveData<Any>, source2 as LiveData<Any>, source3 as LiveData<Any>)
        }

        @MainThread
        @JvmStatic
        fun <T1, T2, T3> combineLatest(source1: LiveData<T1>,
                                       source2: LiveData<T2>,
                                       source3: LiveData<T3>): LiveData<Triple<T1, T2, T3>> {
            return combineLatest(source1, source2, source3) { t1, t2, t3 -> Triple(t1, t2, t3) }
        }
    }
}


fun <T> T.toLiveData(): LiveData<T> =
        LiveDataUtils.just(this)

fun <T, R> LiveData<T>.map(func: Function<T, R>): LiveData<R> =
        LiveDataUtils.map(this, func)

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
        LiveDataUtils.map(this, func)

fun <T, R> LiveData<T>.switchMap(func: Function<T, LiveData<R>>): LiveData<R> =
        LiveDataUtils.switchMap(this, func)

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>): LiveData<R> =
        LiveDataUtils.switchMap(this, func)

fun <T> LiveData<T>.filter(func: Function<T, Boolean>): LiveData<T> =
        LiveDataUtils.filter(this, func)

fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> =
        LiveDataUtils.filter(this, func)

fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
        LiveDataUtils.filterNotNull(this)

fun <T> LiveData<T>.distinctUntilChanged(func: Function<T, Any>): LiveData<T> =
        LiveDataUtils.distinctUntilChanged(this, func)

fun <T> LiveData<T>.distinctUntilChanged(func: (T) -> Any): LiveData<T> =
        LiveDataUtils.distinctUntilChanged(this, func)

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
        LiveDataUtils.distinctUntilChanged(this)

fun <T> Array<LiveData<T>>.merge(): LiveData<T> =
        LiveDataUtils.merge(*this)

fun <T> List<LiveData<T>>.merge(): LiveData<T> =
        LiveDataUtils.merge(*this.toTypedArray())

fun <T> LiveData<T>.mergeWith(other: LiveData<T>):
        LiveData<T> = LiveDataUtils.merge(this, other)

fun <T, R> Array<LiveData<T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        LiveDataUtils.combineLatest(this, combiner)

fun <T, R> List<LiveData<out T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        LiveDataUtils.combineLatest(this.toTypedArray(), combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: Function2<T1, T2, R>): LiveData<R> =
        LiveDataUtils.combineLatest(this, other, combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: (T1, T2) -> R): LiveData<R> =
        LiveDataUtils.combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(other: LiveData<T2>): LiveData<Pair<T1, T2>> =
        LiveDataUtils.combineLatest(this, other) { t1, t2 -> t1 to t2 }
