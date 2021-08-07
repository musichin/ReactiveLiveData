package de.musichin.reactivelivedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class TestLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry.createUnsafe(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    public fun handleLifecycleEvent(event: Lifecycle.Event) {
        lifecycleRegistry.handleLifecycleEvent(event)
    }
}
