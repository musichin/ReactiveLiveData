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
public fun <T> emptyLiveData(): LiveData<T> = object : LiveData<T>() {}

/**
 * Returns new [LiveData] instance that holds given element.
 * @param value element to hold.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> liveDataOf(value: T): LiveData<T> = object : LiveData<T>(value) {}

/**
 * Returns new [LiveData] instance with an element obtained by calling given function when it first becomes active.
 * @param initializer function to invoke to obtain the element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> lazyLiveData(initializer: () -> T): LiveData<T> =
    object : LiveData<T>() {
        private var initialized = false

        override fun onActive() {
            if (initialized) return
            this.value = initializer()
            initialized = true
        }
    }

/**
 * Returns new [LiveData] instance that holds given element.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> T.toLiveData(): LiveData<T> = liveDataOf(this)

/**
 * Returns new [LiveData] instance that obtains its element from given function when it first becomes active.
 * @return new [LiveData] instance.
 */
@MainThread
@CheckResult
public fun <T> (() -> T).toLiveData(): LiveData<T> = lazyLiveData(this)
