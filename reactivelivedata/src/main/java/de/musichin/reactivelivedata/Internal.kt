package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData

internal val NOT_SET = Any()
internal val NEVER = object : LiveData<Any?>() {}
