package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.switchMap as switchMapImpl

/**
 * Returns new [LiveData] instance that switches to the last recent created [LiveData] by applying
 * given `mapper` function to each element emitted.
 * @param mapper function to apply to each element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> LiveData<T>.switchMap(mapper: (T) -> LiveData<R>?): LiveData<R> =
    switchMapImpl(mapper)

/**
 * Returns new [LiveData] instance that switches to the last recent created [LiveData].
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> =
    switchMap { it }
