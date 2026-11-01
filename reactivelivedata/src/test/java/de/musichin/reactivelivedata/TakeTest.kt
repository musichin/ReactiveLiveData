package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertThrows
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

        observer.assertEquals(1)
    }

    @Test
    fun testTake() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.take(2).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(1, 2)
    }

    @Test
    fun testTakeNone() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.take(0).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEmpty()
    }

    @Test
    fun testTakeRejectsNegativeCount() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().take(-1)
        }
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

        observer.assertEquals(1, 2, 3)
    }

    @Test
    fun testTakeUntilOther() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        val other = MutableLiveData<String?>()
        source.takeUntil(other).observeForever(observer)
        source.value = 1
        source.value = 2
        other.value = null
        source.value = 3
        observer.assertEquals(1, 2)

        val sticky = TestObserver<Int>()
        source.takeUntil(other).observeForever(sticky)
        source.value = 4
        sticky.assertEmpty()

        val self = TestObserver<Int>()
        source.takeUntil(source).observeForever(self)
        source.value = 5
        self.assertEmpty()
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

        observer.assertEquals(1, 2, 3)
    }
}
