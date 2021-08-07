package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SkipTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testSkipWhile() {
        val observer = TestObserver<Int>()
        val s = MutableLiveData<Int>()
        s.skipWhile { it >= 3 }.observeForever(observer)
        s.value = 1
        s.value = 2
        s.value = 3
        s.value = 4
        s.value = 5

        observer.assertEquals(3, 4, 5)
    }

    @Test
    fun testSkip() {
        val observer = TestObserver<Int>()
        val s = MutableLiveData<Int>()
        s.skip(2).observeForever(observer)
        s.value = 1
        s.value = 2
        s.value = 3
        s.value = 4
        s.value = 5

        observer.assertEquals(3, 4, 5)
    }
}
