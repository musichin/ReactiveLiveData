package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

class BufferTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testBuffer() {
        val source = MutableLiveData<Int>()
        val observer = TestObserver<List<Int>>()
        source.buffer(2).observeForever(observer)
        for (i in 0..9) source.value = i
        observer.assertEquals(listOf(0, 1), listOf(2, 3), listOf(4, 5), listOf(6, 7), listOf(8, 9))
    }

    @Test
    fun testBufferWithSkip() {
        val source = MutableLiveData<Int>()
        val dropping = TestObserver<List<Int>>()
        val overlapping = TestObserver<List<Int>>()
        source.buffer(2, 3).observeForever(dropping)
        source.buffer(3, 2).observeForever(overlapping)

        source.value = 0
        source.value = 1
        dropping.assertEquals(listOf(0, 1))

        for (i in 2..9) source.value = i
        dropping.assertEquals(listOf(0, 1), listOf(3, 4), listOf(6, 7))
        overlapping.assertEquals(listOf(0, 1, 2), listOf(2, 3, 4), listOf(4, 5, 6), listOf(6, 7, 8))
    }

    @Test
    fun testBufferRejectsZeroCount() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().buffer(0)
        }
    }

    @Test
    fun testBufferRejectsNegativeCount() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().buffer(-1)
        }
    }

    @Test
    fun testBufferRejectsZeroSkip() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().buffer(2, 0)
        }
    }

    @Test
    fun testBufferRejectsNegativeSkip() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().buffer(2, -1)
        }
    }
}
