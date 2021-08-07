package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SwitchTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testSwitchLatest() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<LiveData<Int>>()
        source.switchLatest().observeForever(observer)
        (1..10).forEach { source.value = it.toLiveData() }

        observer.assertEquals(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }
}
