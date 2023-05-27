package de.musichin.reactivelivedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Observes given source and sets emitted values to the observer.
 * @param observer calls [MutableLiveData.setValue] on each emitted element.
 */
@MainThread
fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: MutableLiveData<T>): Unit =
    this.observe(owner, observer::setValue)

/**
 * Observes given source without an observer.
 */
@MainThread
fun <T> LiveData<T>.observe(owner: LifecycleOwner): Unit =
    observe(owner) {}

/**
 * Observes forever given source.
 */
@MainThread
fun <T> LiveData<T>.observeForever(): Unit = this.observeForever {}

/**
 * Observes forever given source and sets emitted values to the observer.
 * @param observer calls [MutableLiveData.setValue] on each emitted element.
 */
@MainThread
fun <T> LiveData<T>.observeForever(observer: MutableLiveData<T>): Unit =
    this.observeForever(observer::setValue)
