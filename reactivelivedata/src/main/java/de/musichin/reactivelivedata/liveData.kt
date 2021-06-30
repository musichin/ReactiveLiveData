package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData

@Suppress("UNCHECKED_CAST")
fun <T> liveData(): LiveData<T> = NEVER as LiveData<T>

fun <T> liveData(value: T): LiveData<T> = object : LiveData<T>() {
    init {
        this.value = value
    }
}

fun <T> liveData(value: () -> T): LiveData<T> = object : LiveData<T>() {
    private var initialized = false
    override fun onActive() {
        if (initialized) return
        this.value = value()
        initialized = true
    }
}

fun <T> T.toLiveData(): LiveData<T> =
    liveData(this)
