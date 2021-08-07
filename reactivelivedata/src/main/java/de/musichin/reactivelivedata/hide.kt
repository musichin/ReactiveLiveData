package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 * Returns same instance that is casted to [LiveData].
 */
fun <T> MutableLiveData<T>.hide(): LiveData<T> = this

/**
 * Returns same instance that is casted to [LiveData].
 */
fun <T> MediatorLiveData<T>.hide(): LiveData<T> = this
