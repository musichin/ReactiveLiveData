package com.github.musichin.livedatautils

import android.arch.core.util.Function
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

fun <T> T.toLiveData(): LiveData<T> =
        LiveDataUtils.just(this)

fun <T, R> LiveData<T>.map(func: Function<T, R>): LiveData<R> =
        Transformations.map(this, func)

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
        Transformations.map(this, func)

fun <T, R> LiveData<T>.switchMap(func: Function<T, LiveData<R>>): LiveData<R> =
        Transformations.switchMap(this, func)

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>): LiveData<R> =
        Transformations.switchMap(this, func)

fun <T> LiveData<T>.filter(func: Function<T, Boolean>): LiveData<T> =
        LiveDataUtils.filter(this, func)

fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> =
        LiveDataUtils.filter(this, func)

fun <T> LiveData<T?>.filterNotNull(): LiveData<T> =
        LiveDataUtils.filterNotNull(this as LiveData<T>)

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

fun <T, R> Array<LiveData<out T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        LiveDataUtils.combineLatest(this, combiner)

fun <T, R> List<LiveData<out T>>.combineLatest(combiner: Function<Array<T>, R>): LiveData<R> =
        LiveDataUtils.combineLatest(this.toTypedArray(), combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: Function2<T1, T2, R>): LiveData<R> =
        LiveDataUtils.combineLatest(this, other, combiner)

fun <T1, T2, R> LiveData<T1>.combineLatestWith(other: LiveData<T2>, combiner: (T1, T2) -> R): LiveData<R> =
        LiveDataUtils.combineLatest(this, other, combiner)

fun <T1, T2> LiveData<T1>.combineLatestWith(other: LiveData<T2>): LiveData<Pair<T1, T2>> =
        LiveDataUtils.combineLatest(this, other) { t1, t2 -> t1 to t2 }
