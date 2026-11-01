package de.musichin.reactivelivedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Observes current source and sets emitted elements to the observer.
 * @param owner [LifecycleOwner] that controls the observation.
 * @param observer calls [MutableLiveData.setValue] on each emitted element.
 */
@MainThread
public fun <T> LiveData<T>.observe(
    owner: LifecycleOwner,
    observer: MutableLiveData<T>,
): Unit = this.observe(owner, observer::setValue)

/**
 * Observes current source without an observer.
 * @param owner [LifecycleOwner] that controls the observation.
 */
@MainThread
public fun <T> LiveData<T>.observe(owner: LifecycleOwner): Unit = observe(owner) {}

/**
 * Observes forever current source.
 * Warning: the observer cannot be removed, so the source stays active forever.
 */
@MainThread
public fun <T> LiveData<T>.observeForever(): Unit = this.observeForever {}

/**
 * Observes forever current source and sets emitted elements to the observer.
 * Warning: the observer cannot be removed, so the source stays active forever.
 * @param observer calls [MutableLiveData.setValue] on each emitted element.
 */
@MainThread
public fun <T> LiveData<T>.observeForever(observer: MutableLiveData<T>): Unit = this.observeForever(observer::setValue)
