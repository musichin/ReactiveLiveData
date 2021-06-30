package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData

@Suppress("UNCHECKED_CAST")
fun <T, U> LiveData<T>.cast(clazz: Class<U>): LiveData<U> =
    map { clazz.cast(it) as U }

inline fun <T, reified U> LiveData<T>.cast(): LiveData<U> =
    cast(U::class.java)
