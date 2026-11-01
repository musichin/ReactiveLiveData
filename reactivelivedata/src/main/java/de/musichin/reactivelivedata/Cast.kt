package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map

/**
 * Returns new [LiveData] instance that casts every emitted element to target class.
 * An element of another type causes a [ClassCastException]; `null` is passed through.
 * @param clazz target class.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
public fun <U> LiveData<*>.cast(clazz: Class<U>): LiveData<U> {
    val objectType = (clazz as Class<*>).kotlin.javaObjectType
    return map { objectType.cast(it) as U }
}

/**
 * Returns new [LiveData] instance that casts every emitted element to target class.
 * An element of another type causes a [ClassCastException]; `null` is passed through.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public inline fun <reified U> LiveData<*>.cast(): LiveData<U> = cast(U::class.java)
