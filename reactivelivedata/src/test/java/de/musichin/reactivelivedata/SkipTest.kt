package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

class SkipTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testSkipUntil() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.skipUntil { it >= 3 }.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 1
        source.value = 5

        observer.assertEquals(3, 1, 5)
    }

    @Test
    fun testSkipWhile() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.skipWhile { it < 3 }.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 1
        source.value = 5

        observer.assertEquals(3, 1, 5)
    }

    @Test
    fun testSkip() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.skip(2).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 4
        source.value = 5

        observer.assertEquals(3, 4, 5)
    }

    @Test
    fun testSkipRejectsNegativeCount() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().skip(-1)
        }
    }
}
