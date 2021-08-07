package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
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
        observer.assertEquals(
            listOf(
                listOf(0, 1),
                listOf(2, 3),
                listOf(4, 5),
                listOf(6, 7),
                listOf(8, 9)
            )
        )
    }

    @Test
    fun testBufferWithSkip() {
        val source = MutableLiveData<Int>()
        val observer = TestObserver<List<Int>>()
        source.buffer(2, 3).observeForever(observer)
        for (i in 0..9) source.value = i
        observer.assertEquals(listOf(listOf(0, 1), listOf(3, 4), listOf(6, 7)))
    }
}
