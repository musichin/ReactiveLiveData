package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.doOnValue(func: (T) -> Unit): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) {
        result.value = it
        func(it as T)
    }
    return result
}

fun <T> LiveData<T>.doOnActive(func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onActive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { result.value = it }
    result.addSource(hook) { }
    return result
}

fun <T> LiveData<T>.doOnInactive(func: () -> Unit): LiveData<T> {
    val hook = object : LiveData<T>() {
        override fun onInactive() = func()
    }

    val result = MediatorLiveData<T>()
    result.addSource(this) { value -> result.value = value }
    result.addSource(hook) { }
    return result
}
