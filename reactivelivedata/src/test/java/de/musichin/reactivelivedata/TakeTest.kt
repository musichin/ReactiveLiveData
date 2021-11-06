package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class TakeTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testFirst() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.first().observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(listOf(1))
    }

    @Test
    fun testTake() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.take(2).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testTakeNone() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.take(-1).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEmpty()
    }

    @Test
    fun testTakeUntil() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.takeUntil { it == 3 }.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 4

        observer.assertEquals(listOf(1, 2, 3))
    }

    @Test
    fun testTakeWhile() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.takeWhile { it <= 3 }.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 4

        observer.assertEquals(listOf(1, 2, 3))
    }
}
