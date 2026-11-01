package de.musichin.reactivelivedata

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Returns this instance typed as [LiveData].
 * @return this instance.
 */
@MainThread
@CheckResult
fun <T> MutableLiveData<T>.hide(): LiveData<T> = this
