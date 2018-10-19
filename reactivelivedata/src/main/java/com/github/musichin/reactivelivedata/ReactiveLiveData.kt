package com.github.musichin.reactivelivedata

import androidx.annotation.MainThread
import androidx.arch.core.util.Function
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

class ReactiveLiveData<T : Any?>(private val source: LiveData<T>) {
    companion object {
        private val NOT_SET = Any()

        @MainThread
        @JvmStatic
        fun <T> hide(source: MutableLiveData<T>): LiveData<T> = source

        @MainThread
        @JvmStatic
        fun <T> of(source: LiveData<T>): ReactiveLiveData<T> = ReactiveLiveData(source)

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
        fun <T, U> cast(source: LiveData<T>, clazz: Class<U>): LiveData<U> {
            return map(source, clazz::cast)
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> buffer(source: LiveData<T>, count: Int, skip: Int): LiveData<List<T>> {
            val buffer: Array<Any?> = arrayOfNulls(if (count > skip) skip else count)
            val result = MediatorLiveData<List<T>>()
            var counter = 0
            result.addSource(source) {
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

        @MainThread
        @JvmStatic
        fun <T> buffer(source: LiveData<T>, count: Int): LiveData<List<T>> {
            return buffer(source, count, count)
        }

        @MainThread
        @JvmStatic
        fun <T> startWith(source: LiveData<T>, value: T): LiveData<T> {
            return startWith(source, just(value))
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
        fun <T> take(source: LiveData<T>, count: Int): LiveData<T> {
            if (count <= 0) return never()
            var counter = 0
            return takeUntil(source) { ++counter >= count }
        }

        @MainThread
        @JvmStatic
        fun <T> takeWhile(source: LiveData<T>, predicate: Function<T, Boolean>): LiveData<T> {
            return takeWhile(source, predicate::apply)
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> takeWhile(source: LiveData<T>, predicate: (T) -> Boolean): LiveData<T> {
            val result = MediatorLiveData<T>()
            result.addSource(source) {
                if (predicate(it as T)) result.value = it
                else result.removeSource(source)
            }
            return result
        }

        @MainThread
        @JvmStatic
        fun <T> takeUntil(source: LiveData<T>, predicate: Function<T, Boolean>): LiveData<T> {
            return takeUntil(source, predicate::apply)
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> takeUntil(source: LiveData<T>, predicate: (T) -> Boolean): LiveData<T> {
            val result = MediatorLiveData<T>()
            result.addSource(source) {
                if (predicate(it as T)) result.removeSource(source)
                result.value = it
            }
            return result
        }

        @MainThread
        @JvmStatic
        fun <T> first(source: LiveData<T>): LiveData<T> {
            return take(source, 1)
        }

        @MainThread
        @JvmStatic
        fun <T, R> map(source: LiveData<T>, func: (T) -> R): LiveData<R> {
            return Transformations.map(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T, R> switchMap(source: LiveData<T>, func: Function<T, LiveData<R>?>): LiveData<R> {
            return Transformations.switchMap(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T, R> switchMap(source: LiveData<T>, func: (T) -> LiveData<R>?): LiveData<R> {
            return Transformations.switchMap(source, func)
        }

        @MainThread
        @JvmStatic
        fun <T> switchLatest(source: LiveData<out LiveData<T>?>): LiveData<T> {
            return switchMap(source) { it }
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
        fun <T> filterNot(source: LiveData<T>, func: Function<T, Boolean>): LiveData<T> {
            return filterNot(source, func::apply)
        }

        @MainThread
        @JvmStatic
        fun <T> filterNot(source: LiveData<T>, func: (T) -> Boolean): LiveData<T> {
            return filter(source) { !func(it) }
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> filterNotNull(source: LiveData<T?>): LiveData<T> {
            return filter(source) { it != null } as LiveData<T>
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T> filterIsInstance(source: LiveData<*>, clazz: Class<T>): LiveData<T> {
            return filter(source) { clazz.isInstance(it) } as LiveData<T>
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
            return distinctUntilChanged(source) { it }
        }

        @MainThread
        @JvmStatic
        fun <T> merge(vararg sources: LiveData<T>): LiveData<T> {
            if (sources.size <= 0) return ReactiveLiveData.never()

            val result = MediatorLiveData<T>()

            val observer = Observer<T> { result.value = it }

            for (source in sources) {
                result.addSource(source, observer)
            }
            return result
        }

        @MainThread
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T, R> combineLatest(sources: Array<out LiveData<out T>>, combiner: Function<Array<T>, R>): LiveData<R> {
            if (sources.size <= 0) return ReactiveLiveData.never()

            val size = sources.size
            val result = MediatorLiveData<Any>()

            val values = arrayOfNulls<Any?>(size)
            for (index in 0..size - 1) values[index] = NOT_SET

            var emits = 0
            for (index in 0..size - 1) {
                val observer = Observer<Any> { t ->
                    var combine = emits == size
                    if (!combine) {
                        if (values[index] === NOT_SET) emits++
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
        fun <T> doOnActive(source: LiveData<T>, func: () -> Unit): LiveData<T> {
            val hook = object : LiveData<T>() {
                override fun onActive() = func()
            }

            val result = MediatorLiveData<T>()
            result.addSource(source) { result.value = it }
            result.addSource(hook) { }
            return result
        }

        @MainThread
        @JvmStatic
        fun <T> doOnInactive(source: LiveData<T>, func: () -> Unit): LiveData<T> {
            val hook = object : LiveData<T>() {
                override fun onInactive() = func()
            }

            val result = MediatorLiveData<T>()
            result.addSource(source) { result.value = it }
            result.addSource(hook) { }
            return result
        }
    }

    fun liveData(): LiveData<T> = source

    fun observe(owner: LifecycleOwner, observer: MutableLiveData<T>) =
            ReactiveLiveData.observe(source, owner, observer)

    fun observe(owner: LifecycleOwner) =
            ReactiveLiveData.observe(source, owner)

    fun observe(owner: LifecycleOwner, observer: Observer<T>) =
            ReactiveLiveData.observe(source, owner, observer)

    fun observeForever() =
            ReactiveLiveData.observeForever(source)

    fun observeForever(observer: Observer<T>) =
            ReactiveLiveData.observeForever(source, observer)

    fun observeForever(observer: MutableLiveData<T>) =
            ReactiveLiveData.observeForever(source, observer)

    fun observeForever(observer: (T) -> Unit) =
            ReactiveLiveData.observeForever(source, observer)

    fun toLiveData(): LiveData<T> = source

    fun <U> cast(clazz: Class<U>): ReactiveLiveData<U> =
            ReactiveLiveData(ReactiveLiveData.cast(source, clazz))

    fun buffer(count: Int): ReactiveLiveData<List<T>> =
            ReactiveLiveData(ReactiveLiveData.buffer(source, count))

    fun buffer(count: Int, skip: Int): ReactiveLiveData<List<T>> =
            ReactiveLiveData(ReactiveLiveData.buffer(source, count, skip))

    fun startWith(value: T): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.startWith(source, value))

    fun startWith(value: () -> T): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.startWith(source, value))

    fun startWith(value: LiveData<T>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.startWith(source, value))

    fun take(count: Int): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.take(source, count))

    fun first(): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.first(source))

    fun takeUntil(predicate: Function<T, Boolean>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.takeUntil(source, predicate))

    fun takeWhile(predicate: Function<T, Boolean>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.takeWhile(source, predicate))

    fun <R> switchMap(func: Function<T, LiveData<R>?>): ReactiveLiveData<R> =
            ReactiveLiveData(ReactiveLiveData.switchMap(source, func))

    fun filter(func: Function<T, Boolean>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.filter(source, func))

    fun filterNot(func: Function<T, Boolean>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.filterNot(source, func))

    @Suppress("UNCHECKED_CAST")
    fun filterNotNull(): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.filterNotNull(source as LiveData<T?>))

    fun <T> filterIsInstance(clazz: Class<T>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.filterIsInstance(source, clazz))

    fun <K> distinct(func: Function<T, K>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.distinct(source, func))

    fun distinct(): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.distinct(source))

    fun <K> distinctUntilChanged(func: Function<T, K>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.distinctUntilChanged(source, func))

    fun distinctUntilChanged(): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.distinctUntilChanged(source))

    fun mergeWith(other: LiveData<T>): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.merge(source, other))

    fun doOnValue(func: (T) -> Unit): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.doOnValue(source, func))

    fun doOnActive(func: () -> Unit): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.doOnActive(source, func))

    fun doOnInactive(func: () -> Unit): ReactiveLiveData<T> =
            ReactiveLiveData(ReactiveLiveData.doOnInactive(source, func))
}

fun <T> MutableLiveData<T>.hide() =
        ReactiveLiveData.hide(this)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: MutableLiveData<T>) =
        ReactiveLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observe(owner: LifecycleOwner) =
        ReactiveLiveData.observe(this, owner)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: Observer<T>) =
        ReactiveLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: Function<T, Unit>) =
        ReactiveLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        ReactiveLiveData.observe(this, owner, observer)

fun <T> LiveData<T>.observeForever() =
        ReactiveLiveData.observeForever(this)

fun <T> LiveData<T>.observeForever(observer: Observer<T>) =
        ReactiveLiveData.observeForever(this, observer)

fun <T> LiveData<T>.observeForever(observer: MutableLiveData<T>) =
        ReactiveLiveData.observeForever(this, observer)

fun <T> LiveData<T>.observeForever(observer: (T) -> Unit) =
        ReactiveLiveData.observeForever(this, observer)

fun <T> T.toLiveData(): LiveData<T> =
        ReactiveLiveData.just(this)

fun <T, U> LiveData<T>.cast(clazz: Class<U>): LiveData<U> =
        ReactiveLiveData.cast(this, clazz)

fun <T> LiveData<T>.buffer(count: Int): LiveData<List<T>> =
        ReactiveLiveData.buffer(this, count)

fun <T> LiveData<T>.buffer(count: Int, skip: Int): LiveData<List<T>> =
        ReactiveLiveData.buffer(this, count, skip)

fun <T> LiveData<T>.startWith(value: T): LiveData<T> =
        ReactiveLiveData.startWith(this, value)

fun <T> LiveData<T>.startWith(value: () -> T): LiveData<T> =
        ReactiveLiveData.startWith(this, value())

fun <T> LiveData<T>.startWith(value: LiveData<T>): LiveData<T> =
        ReactiveLiveData.startWith(this, value)

fun <T> LiveData<T>.take(count: Int): LiveData<T> =
        ReactiveLiveData.take(this, count)

fun <T> LiveData<T>.first(): LiveData<T> =
        ReactiveLiveData.first(this)

fun <T> LiveData<T>.takeUntil(predicate: (T) -> Boolean): LiveData<T> =
        ReactiveLiveData.takeUntil(this, predicate)

fun <T> LiveData<T>.takeUntil(predicate: Function<T, Boolean>): LiveData<T> =
        ReactiveLiveData.takeUntil(this, predicate)

fun <T> LiveData<T>.takeWhile(predicate: (T) -> Boolean): LiveData<T> =
        ReactiveLiveData.takeWhile(this, predicate)

fun <T> LiveData<T>.takeWhile(predicate: Function<T, Boolean>): LiveData<T> =
        ReactiveLiveData.takeWhile(this, predicate)

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
        ReactiveLiveData.map(this, func)

fun <T, R> LiveData<T>.switchMap(func: Function<T, LiveData<R>?>): LiveData<R> =
        ReactiveLiveData.switchMap(this, func)

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>?): LiveData<R> =
        ReactiveLiveData.switchMap(this, func)

fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> =
        ReactiveLiveData.switchLatest(this)

fun <T> LiveData<T>.filter(func: Function<T, Boolean>): LiveData<T> =
        ReactiveLiveData.filter(this, func)

fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> =
        ReactiveLiveData.filter(this, func)

fun <T> LiveData<T>.filterNot(func: Function<T, Boolean>): LiveData<T> =
        ReactiveLiveData.filterNot(this, func)

fun <T> LiveData<T>.filterNot(func: (T) -> Boolean): LiveData<T> =
        ReactiveLiveData.filterNot(this, func)

fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
        ReactiveLiveData.filterNotNull(this)

fun <T> LiveData<*>.filterIsInstance(clazz: Class<T>): LiveData<T> =
        ReactiveLiveData.filterIsInstance(this, clazz)

inline fun <reified T> LiveData<*>.filterIsInstance(): LiveData<T> =
        ReactiveLiveData.filterIsInstance(this, T::class.java)

fun <T, K> LiveData<T>.distinct(func: Function<T, K>): LiveData<T> =
        ReactiveLiveData.distinct(this, func)

fun <T, K> LiveData<T>.distinct(func: (T) -> K): LiveData<T> =
        ReactiveLiveData.distinct(this, func)

fun <T> LiveData<T>.distinct(): LiveData<T> =
        ReactiveLiveData.distinct(this)

fun <T, K> LiveData<T>.distinctUntilChanged(func: Function<T, K>): LiveData<T> =
        ReactiveLiveData.distinctUntilChanged(this, func)

fun <T, K> LiveData<T>.distinctUntilChanged(func: (T) -> K): LiveData<T> =
        ReactiveLiveData.distinctUntilChanged(this, func)

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> =
        ReactiveLiveData.distinctUntilChanged(this)

fun <T> Array<LiveData<T>>.merge(): LiveData<T> =
        ReactiveLiveData.merge(*this)

operator fun <T> LiveData<T>.plus(other: LiveData<T>): LiveData<T> =
        mergeWith(other)

fun <T> Collection<LiveData<T>>.merge(): LiveData<T> =
        ReactiveLiveData.merge(*this.toTypedArray())

fun <T> LiveData<T>.mergeWith(other: LiveData<T>): LiveData<T> =
        ReactiveLiveData.merge(this, other)

fun <T, R> Array<LiveData<T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        ReactiveLiveData.combineLatest(this, combiner)

fun <T, R> Collection<LiveData<out T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        ReactiveLiveData.combineLatest(this.toTypedArray(), combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: (T1, T2) -> R): LiveData<R> =
        ReactiveLiveData.combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(other: LiveData<T2>): LiveData<Pair<T1, T2>> =
        ReactiveLiveData.combineLatest(this, other) { t1, t2 -> t1 to t2 }

fun <T> LiveData<T>.doOnValue(func: (T) -> Unit): LiveData<T> =
        ReactiveLiveData.doOnValue(this, func)

fun <T> LiveData<T>.doOnActive(func: () -> Unit): LiveData<T> =
        ReactiveLiveData.doOnActive(this, func)

fun <T> LiveData<T>.doOnInactive(func: () -> Unit): LiveData<T> =
        ReactiveLiveData.doOnInactive(this, func)
