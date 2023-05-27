package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Returns same instance that is cast to [LiveData].
 */
@MainThread
@CheckResult
fun <T> MutableLiveData<T>.hide(): LiveData<T> = this
