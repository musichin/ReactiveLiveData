package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class AmbTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testAmb() {
        val observer = TestObserver<Int>()
        val source0 = MutableLiveData<Int>()
        val source1 = MutableLiveData<Int>()
        listOf(source0, source1, source0).amb().observeForever(observer)
        source1.value = 1
        source0.value = 2
        source1.value = 3
        observer.assertEquals(1, 3)

        val sticky = TestObserver<Int>()
        arrayOf(source0, source1).amb().observeForever(sticky)
        source1.value = 4
        source0.value = 5
        sticky.assertEquals(2, 5)

        val empty = TestObserver<Int>()
        emptyList<LiveData<Int>>().amb().observeForever(empty)
        empty.assertEmpty()
    }

    @Test
    fun testAmbWith() {
        val observer = TestObserver<Int>()
        val source0 = MutableLiveData<Int>()
        val source1 = MutableLiveData<Int>()
        source0.ambWith(source1).observeForever(observer)
        source1.value = 1
        source0.value = 2
        source1.value = 3
        observer.assertEquals(1, 3)
    }
}
