package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

private fun <T> LiveData<T>.mergeWith(other: Iterator<LiveData<T>>): LiveData<T> {
    val result = MediatorLiveData<T>()

    result.addSource(this, result::setValue)
    other.forEach { source ->
        result.addSource(source, result::setValue)
    }

    return result
}

private fun <T> Iterator<LiveData<T>>.mergeWith(): LiveData<T> {
    if (!hasNext()) return liveData()
    return next().mergeWith(this)
}

fun <T> Iterable<LiveData<T>>.merge(): LiveData<T> =
    iterator().mergeWith()

fun <T> merge(vararg sources: LiveData<T>): LiveData<T> =
    sources.iterator().mergeWith()

fun <T> LiveData<T>.mergeWith(other: LiveData<T>): LiveData<T> =
    merge(this, other)

fun <T> LiveData<T>.mergeWith(other: Iterable<LiveData<T>>): LiveData<T> =
    mergeWith(other.iterator())

fun <T> LiveData<T>.mergeWith(other: Array<LiveData<T>>): LiveData<T> =
    mergeWith(other.iterator())

operator fun <T> LiveData<T>.plus(other: LiveData<T>): LiveData<T> =
    mergeWith(other)
