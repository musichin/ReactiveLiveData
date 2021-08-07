package de.musichin.reactivelivedata

import androidx.lifecycle.LiveData

internal val NOT_SET = Any()
internal val NEVER = object : LiveData<Any?>() {}
internal inline val <T> ((T) -> Boolean).not: (T) -> Boolean
    get() = { value ->
        !invoke(value)
    }
