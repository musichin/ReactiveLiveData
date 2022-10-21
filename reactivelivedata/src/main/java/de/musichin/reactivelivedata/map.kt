package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/**
 * Returns new [LiveData] instance that emits elements after applying given `transform`
 * function to each element in the original [LiveData].
 * @return new [LiveData] instance.
 */
@CheckResult
fun <T, R> LiveData<T>.map(transform: (T) -> R): LiveData<R> =
    Transformations.map(this, transform)
