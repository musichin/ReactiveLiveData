package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ObserveTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testObserve() {
        val owner = TestLifecycleOwner()
        val source = liveData { "test" }
        val observer = MutableLiveData<String>()

        assertFalse(source.hasActiveObservers())
        assertNull(observer.value)

        owner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        source.observe(owner, observer)
        assertTrue(source.hasActiveObservers())
        assertEquals("test", observer.value)
    }

    @Test
    fun testObserveMutableLiveData() {
        val owner = TestLifecycleOwner()
        val source = liveData<Any> { "test" }

        assertFalse(source.hasActiveObservers())

        owner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        source.observe(owner)
        assertTrue(source.hasActiveObservers())
    }

    @Test
    fun testObserveForever() {
        val source = liveData<Any> { "test" }

        source.observeForever()

        assertTrue(source.hasActiveObservers())
    }

    @Test
    fun testObserveForeverMutableLiveData() {
        val source = liveData { "test" }
        val observer = MutableLiveData<String>()

        source.observeForever(observer)

        assertTrue(source.hasActiveObservers())
        assertEquals("test", observer.value)
    }
}
