package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DoOnTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testDoOnValue() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()

        val onValueCollection = mutableListOf<Int>()
        val onValueCollector: (Int) -> Unit = {
            onValueCollection.add(it)
        }
        val monitoredSource = source.doOnValue(onValueCollector)
        monitoredSource.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(1, 2, 3)
        assertEquals(listOf(1, 2, 3), onValueCollection)
    }

    @Test
    fun testDoOnValueRunsBeforeObservers() {
        val events = mutableListOf<String>()
        val source = MutableLiveData<Int>()
        source
            .doOnValue { events.add("onValue $it") }
            .observeForever { events.add("observer $it") }

        source.value = 1
        source.value = 2

        assertEquals(listOf("onValue 1", "observer 1", "onValue 2", "observer 2"), events)
    }

    @Test
    fun testDoOnActiveAndDoOnInactive() {
        val observer = TestObserver<Int>()
        var active = false
        val source = MutableLiveData<Int>()
        val monitoredSource = source.doOnActive { active = true }.doOnInactive { active = false }
        monitoredSource.observeForever(observer)
        source.value = 1
        assertTrue(active)
        monitoredSource.removeObserver(observer)
        source.value = 2
        assertFalse(active)
        monitoredSource.observeForever(observer)
        source.value = 3
        assertTrue(active)
        monitoredSource.removeObserver(observer)
        source.value = 4
        assertFalse(active)

        observer.assertEquals(1, 2, 3)
    }
}
