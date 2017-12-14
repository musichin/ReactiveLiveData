package com.github.musichin.reactlivedata

import android.arch.core.util.Function
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.support.annotation.MainThread

object ReactLiveData {
    private val NOT_SET = Any()


    @MainThread
    @JvmStatic
    fun <T> observe(source: LiveData<T>, owner: LifecycleOwner) {
        source.observe(owner, Observer { })
    }

    @MainThread
    @JvmStatic
    fun <T> observe(source: LiveData<T>, owner: LifecycleOwner, observer: Observer<T>) {
        source.observe(owner, observer)
    }

    @MainThread
    @JvmStatic
    fun <T> observe(source: LiveData<T>, owner: LifecycleOwner, observer: Function<T, Unit>) {
        observe(source, owner, observer::apply)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> observe(source: LiveData<T>, owner: LifecycleOwner, observer: (T) -> Unit) {
        source.observe(owner, Observer { observer(it as T) })
    }

    @MainThread
    @JvmStatic
    fun <T> observe(source: LiveData<T>, owner: LifecycleOwner, observer: MutableLiveData<T>) {
        source.observe(owner, Observer { observer.value = it })
    }


    @MainThread
    @JvmStatic
    fun <T> observeForever(source: LiveData<T>, observer: MutableLiveData<T>) {
        source.observeForever { observer.value = it }
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> observeForever(source: LiveData<T>) {
        source.observeForever { }
    }

    @MainThread
    @JvmStatic
    fun <T> observeForever(source: LiveData<T>, observer: Observer<T>) {
        source.observeForever(observer)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> observeForever(source: LiveData<T>, observer: Function<T, Unit>) {
        observeForever(source, observer::apply)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> observeForever(source: LiveData<T>, observer: (T) -> Unit) {
        source.observeForever { observer(it as T) }
    }

    @MainThread
    @JvmStatic
    fun <T> never(): LiveData<T> {
        return object : LiveData<T>() {}
    }

    @MainThread
    @JvmStatic
    fun <T> just(value: T): LiveData<T> {
        return object : LiveData<T>() {
            init {
                this.value = value
            }
        }
    }

    @MainThread
    @JvmStatic
    fun <T> create(value: Function<Unit, T>): LiveData<T> {
        return create { value.apply(null) }
    }

    @MainThread
    @JvmStatic
    fun <T> create(value: () -> T): LiveData<T> {
        return object : LiveData<T>() {
            private var initialized = false
            override fun onActive() {
                if (initialized) return
                this.value = value()
                initialized = true
            }
        }
    }

    @MainThread
    @JvmStatic
    fun <T> startWith(source: LiveData<T>, value: T): LiveData<T> {
        return startWith(source, just(value))
    }

    @MainThread
    @JvmStatic
    fun <T> startWith(source: LiveData<T>, value: Function<Unit, T>): LiveData<T> {
        return startWith(source, create(value))
    }

    @MainThread
    @JvmStatic
    fun <T> startWith(source: LiveData<T>, value: () -> T): LiveData<T> {
        return startWith(source, create(value))
    }

    @MainThread
    @JvmStatic
    fun <T> startWith(source: LiveData<T>, value: LiveData<T>): LiveData<T> {
        val result = MediatorLiveData<T>()
        result.addSource(value) {
            result.removeSource(value)
            result.value = it
            result.addSource(source) { result.value = it }
        }
        return result
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
        result.addSource(source) { if (func(it as T)) result.value = it }
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
    fun <T, K> distinctUntilChanged(source: LiveData<T>, func: Function<T, K>): LiveData<T> {
        return distinctUntilChanged(source, func::apply)
    }

    @MainThread
    @JvmStatic
    fun <T, K> distinctUntilChanged(source: LiveData<T>, func: (T) -> K): LiveData<T> {
        var prev: Any? = NOT_SET
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
    fun <T, K> distinct(source: LiveData<T>, func: Function<T, K>): LiveData<T> {
        return distinct(source, func::apply)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T, K> distinct(source: LiveData<T>, func: (T) -> K): LiveData<T> {
        val keys = HashSet<Any?>()
        return filter(source) { keys.add(func(it)) }
    }

    @MainThread
    @JvmStatic
    fun <T> distinct(source: LiveData<T>): LiveData<T> {
        return distinct(source) { it }
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
            return ReactLiveData.never()
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
            return ReactLiveData.never()
        }

        val size = sources.size
        val result = MediatorLiveData<Any>()

        val values = arrayOfNulls<Any?>(size)
        for (index in 0..size - 1) values[index] = NOT_SET

        var emits = 0
        for (index in 0..size - 1) {
            val observer = Observer<Any> { t ->
                var combine = emits == size
                if (!combine) {
                    if (values[index] == NOT_SET) emits++
                    combine = emits == size
                }
                values[index] = t

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
        val func = Function<Array<Any>, R> { input ->
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
        val func = Function<Array<Any>, R> { input ->
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
    fun <T1, T2, T3, T4, R> combineLatest(source1: LiveData<T1>,
                                          source2: LiveData<T2>,
                                          source3: LiveData<T3>,
                                          source4: LiveData<T4>,
                                          combiner: Function4<T1, T2, T3, T4, R>): LiveData<R> {
        return combineLatest(source1, source2, source3, source4, combiner::apply)
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
        val func = Function<Array<Any>, R> { input ->
            combiner(input[0] as T1, input[1] as T2, input[2] as T3, input[3] as T4, input[3] as T5)
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
    fun <T1, T2, T3, T4, T5, R> combineLatest(source1: LiveData<T1>,
                                              source2: LiveData<T2>,
                                              source3: LiveData<T3>,
                                              source4: LiveData<T4>,
                                              source5: LiveData<T5>,
                                              combiner: Function5<T1, T2, T3, T4, T5, R>): LiveData<R> {
        return combineLatest(source1, source2, source3, source4, source5, combiner::apply)
    }

    @MainThread
    @JvmStatic
    fun <T1, T2, T3, T4, T5, T6, T7, R> combineLatest(
            source1: LiveData<T1>,
            source2: LiveData<T2>,
            source3: LiveData<T3>,
            source4: LiveData<T4>,
            source5: LiveData<T5>,
            source6: LiveData<T6>,
            source7: LiveData<T7>,
            combiner: Function7<T1, T2, T3, T4, T5, T6, T7, R>): LiveData<R> {
        return combineLatest(source1, source2, source3, source4, source5, source6, source7, combiner::apply)
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
        val func = Function<Array<Any>, R> { input ->
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
        val func = Function<Array<Any>, R> { input ->
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
    fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineLatest(
            source1: LiveData<T1>,
            source2: LiveData<T2>,
            source3: LiveData<T3>,
            source4: LiveData<T4>,
            source5: LiveData<T5>,
            source6: LiveData<T6>,
            source7: LiveData<T7>,
            source8: LiveData<T8>,
            combiner: Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>): LiveData<R> {
        return combineLatest(source1, source2, source3, source4, source5, source6, source7, source8, combiner::apply)
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
        val func = Function<Array<Any>, R> { input ->
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
            combiner: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>): LiveData<R> {
        return combineLatest(source1, source2, source3, source4, source5, source6, source7, source8, source9,
                combiner::apply)
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
        val func = Function<Array<Any>, R> { input ->
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
            combiner: Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>): LiveData<R> {
        return combineLatest(source1, source2, source3, source4, source5, source6, source7, source8, source9, source10,
                combiner::apply)
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
        val func = Function<Array<Any>, R> { input ->
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
    fun <T> doOnValue(source: LiveData<T>, func: Function<T, Unit>): LiveData<T> {
        return doOnValue(source, func::apply)
    }

    @MainThread
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> doOnValue(source: LiveData<T>, func: (T) -> Unit): LiveData<T> {
        val result = MediatorLiveData<T>()
        result.addSource(source) {
            result.value = it
            func(it as T)
        }
        return result
    }

    @MainThread
    @JvmStatic
    fun <T> doOnActive(source: LiveData<T>, func: Function<Unit, Unit>): LiveData<T> {
        return doOnActive(source, func::apply)
    }

    @MainThread
    @JvmStatic
    fun <T> doOnActive(source: LiveData<T>, func: (Unit) -> Unit): LiveData<T> {
        val hook = object : LiveData<T>() {
            override fun onActive() = func(Unit)
        }

        val result = MediatorLiveData<T>()
        result.addSource(source) { result.value = it }
        result.addSource(hook) { }
        return result
    }

    @MainThread
    @JvmStatic
    fun <T> doOnInactive(source: LiveData<T>, func: Function<Unit, Unit>): LiveData<T> {
        return doOnInactive(source, func::apply)
    }

    @MainThread
    @JvmStatic
    fun <T> doOnInactive(source: LiveData<T>, func: (Unit) -> Unit): LiveData<T> {
        val hook = object : LiveData<T>() {
            override fun onInactive() = func(Unit)
        }

        val result = MediatorLiveData<T>()
        result.addSource(source) { result.value = it }
        result.addSource(hook) { }
        return result
    }
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: MutableLiveData<T>) =
        ReactLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observe(owner: LifecycleOwner) =
        ReactLiveData.observe(this, owner)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: Observer<T>) =
        ReactLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: Function<T, Unit>) =
        ReactLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        ReactLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observeForever() =
        ReactLiveData.observeForever(this)

fun <T> LiveData<T>.observeForever(observer: Observer<T>) =
        ReactLiveData.observeForever(this, observer)

fun <T> LiveData<T>.observeForever(observer: MutableLiveData<T>) =
        ReactLiveData.observeForever(this, observer)

fun <T> LiveData<T>.observeForever(observer: Function<T, Unit>) =
        ReactLiveData.observeForever(this, observer)

fun <T> LiveData<T>.observeForever(observer: (T) -> Unit) =
        ReactLiveData.observeForever(this, observer)

fun <T> T.toLiveData(): LiveData<T> =
        ReactLiveData.just(this)

fun <T> LiveData<T>.startWith(value: T) =
        ReactLiveData.startWith(this, value)

fun <T> LiveData<T>.startWith(value: () -> T) =
        ReactLiveData.startWith(this, value)

fun <T> LiveData<T>.startWith(value: Function<Unit, T>) =
        ReactLiveData.startWith(this, value)

fun <T> LiveData<T>.startWith(value: LiveData<T>) =
        ReactLiveData.startWith(this, value)

fun <T, R> LiveData<T>.map(func: Function<T, R>): LiveData<R> =
        ReactLiveData.map(this, func)

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
        ReactLiveData.map(this, func)

fun <T, R> LiveData<T>.switchMap(func: Function<T, LiveData<R>>): LiveData<R> =
        ReactLiveData.switchMap(this, func)

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>): LiveData<R> =
        ReactLiveData.switchMap(this, func)

fun <T> LiveData<T>.filter(func: Function<T, Boolean>): LiveData<T> =
        ReactLiveData.filter(this, func)

fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> =
        ReactLiveData.filter(this, func)

fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
        ReactLiveData.filterNotNull(this)

fun <T, K> LiveData<T>.distinct(func: Function<T, K>): LiveData<T> =
        ReactLiveData.distinct(this, func)

fun <T, K> LiveData<T>.distinct(func: (T) -> K): LiveData<T> =
        ReactLiveData.distinct(this, func)

fun <T> LiveData<T>.distinct(): LiveData<T> =
        ReactLiveData.distinct(this)

fun <T, K> LiveData<T>.distinctUntilChanged(func: Function<T, K>): LiveData<T> =
        ReactLiveData.distinctUntilChanged(this, func)

fun <T, K> LiveData<T>.distinctUntilChanged(func: (T) -> K): LiveData<T> =
        ReactLiveData.distinctUntilChanged(this, func)

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
        ReactLiveData.distinctUntilChanged(this)

fun <T> Array<LiveData<T>>.merge(): LiveData<T> =
        ReactLiveData.merge(*this)

fun <T> List<LiveData<T>>.merge(): LiveData<T> =
        ReactLiveData.merge(*this.toTypedArray())

fun <T> LiveData<T>.mergeWith(other: LiveData<T>):
        LiveData<T> = ReactLiveData.merge(this, other)

fun <T, R> Array<LiveData<T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        ReactLiveData.combineLatest(this, combiner)

fun <T, R> List<LiveData<out T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        ReactLiveData.combineLatest(this.toTypedArray(), combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: Function2<T1, T2, R>): LiveData<R> =
        ReactLiveData.combineLatest(this, other, combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: (T1, T2) -> R): LiveData<R> =
        ReactLiveData.combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(other: LiveData<T2>): LiveData<Pair<T1, T2>> =
        ReactLiveData.combineLatest(this, other) { t1, t2 -> t1 to t2 }

fun <T> LiveData<T>.doOnValue(func: Function<T, Unit>): LiveData<T> =
        ReactLiveData.doOnValue(this, func)

fun <T> LiveData<T>.doOnValue(func: (T) -> Unit): LiveData<T> =
        ReactLiveData.doOnValue(this, func)

fun <T> LiveData<T>.doOnActive(func: Function<Unit, Unit>): LiveData<T> =
        ReactLiveData.doOnActive(this, func)

fun <T> LiveData<T>.doOnActive(func: (Unit) -> Unit): LiveData<T> =
        ReactLiveData.doOnActive(this, func)

fun <T> LiveData<T>.doOnInactive(func: Function<Unit, Unit>): LiveData<T> =
        ReactLiveData.doOnInactive(this, func)

fun <T> LiveData<T>.doOnInactive(func: (Unit) -> Unit): LiveData<T> =
        ReactLiveData.doOnInactive(this, func)
