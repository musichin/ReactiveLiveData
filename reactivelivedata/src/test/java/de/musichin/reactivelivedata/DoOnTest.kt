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
    fun testOnValue() {
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
    fun testOnActiveAndOnInactive() {
        val observer = TestObserver<Int>()
        var active = false
        val source = MutableLiveData<Int>()
        val monitoredSource = source.doOnActive { active = true }.doOnInactive { active = false }
        monitoredSource.observeForever(observer)
        assertTrue(active)
        monitoredSource.removeObserver(observer)
        assertFalse(active)
        monitoredSource.observeForever(observer)
        assertTrue(active)
    }
}
