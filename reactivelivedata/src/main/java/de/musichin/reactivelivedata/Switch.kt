package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap

/**
 * Returns new [LiveData] instance that switches to the most recently created [LiveData].
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> LiveData<out LiveData<T>?>.switchLatest(): LiveData<T> = switchMap { it }
