package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData

/**
 * Returns new [LiveData] instance that never emits any elements.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <T> liveData(): LiveData<T> = NEVER as LiveData<T>

/**
 * Returns new [LiveData] instance that holds given element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> liveData(value: T): LiveData<T> = object : LiveData<T>(value) {}

/**
 * Returns new [LiveData] instance with an element obtained by calling given function when it gets active.
 * @param value function to invoke to obtain the value.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> liveData(value: () -> T): LiveData<T> = object : LiveData<T>() {
    private var initialized = false
    override fun onActive() {
        if (initialized) return
        this.value = value()
        initialized = true
    }
}

/**
 * Returns new [LiveData] instance that holds given element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> T.toLiveData(): LiveData<T> =
    liveData(this)

/**
 * Returns new [LiveData] instance that obtains its value when it first gets active from given function.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
fun <T> (() -> T).toLiveData(): LiveData<T> =
    liveData(this)
