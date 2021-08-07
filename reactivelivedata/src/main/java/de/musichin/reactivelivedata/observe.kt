package de.musichin.reactivelivedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: MutableLiveData<T>) =
    this.observe(owner, observer::setValue)

fun <T> LiveData<T>.observe(owner: LifecycleOwner) =
    observe(owner) {}

fun <T> LiveData<T>.observeForever() = this.observeForever {}

fun <T> LiveData<T>.observeForever(observer: MutableLiveData<T>) =
    this.observeForever(observer::setValue)
