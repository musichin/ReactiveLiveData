package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map as mapImpl

/**
 * Returns new [LiveData] instance that emits elements after applying given `transform`
 * function to each element in the original [LiveData].
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T, R> LiveData<T>.map(transform: (T) -> R): LiveData<R> =
    mapImpl(transform)
