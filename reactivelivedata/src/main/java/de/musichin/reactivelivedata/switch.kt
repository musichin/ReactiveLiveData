package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, R> LiveData<T>.switchMap(func: (T) -> LiveData<R>?): LiveData<R> =
    Transformations.switchMap(this, func)

fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> =
    switchMap { it }
