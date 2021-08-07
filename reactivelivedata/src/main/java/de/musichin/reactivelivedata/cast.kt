package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData

/**
 * Returns new [LiveData] instance that converts every emitted element to target class.
 * @param clazz target class.
 * @return new [LiveData] instance.
 */
fun <U> LiveData<*>.cast(clazz: Class<U>): LiveData<U> =
    map { clazz.cast(it) as U }

/**
 * Returns new [LiveData] instance that converts every emitted element to target class.
 * @return new [LiveData] instance.
 */
inline fun <reified U> LiveData<*>.cast(): LiveData<U> =
    cast(U::class.java)
