package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/**
 * Returns new [LiveData] instance that switches to the last recent created [LiveData] by applying
 * given `mapper` function to each element emitted.
 * @param mapper function to apply to each element.
 * @return new [LiveData] instance.
 */
fun <T, R> LiveData<T>.switchMap(mapper: (T) -> LiveData<R>?): LiveData<R> =
    Transformations.switchMap(this, mapper)

/**
 * Returns new [LiveData] instance that switches to the last recent created [LiveData].
 * @return new [LiveData] instance.
 */
fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> =
    switchMap { it }
