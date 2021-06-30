package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> =
    Transformations.map(this, func)
