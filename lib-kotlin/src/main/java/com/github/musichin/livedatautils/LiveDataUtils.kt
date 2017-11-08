package com.github.musichin.livedatautils

import android.arch.core.util.Function
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

fun <T, R> LiveData<T>.map(func: Function<T, R>): LiveData<R> = Transformations.map(this, func)
fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> = Transformations.map(this, func)
fun <T> LiveData<T>.filter(func: Function<T, Boolean>): LiveData<T> = LiveDataUtils.filter(this, func)
fun <T> LiveData<T>.filter(func: (T) -> Boolean): LiveData<T> = LiveDataUtils.filter(this, func)
fun <T> LiveData<T?>.filterNotNull(): LiveData<T> = LiveDataUtils.filterNotNull(this as LiveData<T>)
fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = LiveDataUtils.distinctUntilChanged(this)
