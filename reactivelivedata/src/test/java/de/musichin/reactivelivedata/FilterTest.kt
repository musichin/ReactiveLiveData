package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FilterTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testFilter() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.filter { it % 2 == 0 }.observeForever(observer)
        (1..10).forEach { data.value = it }

        observer.assertEquals(2, 4, 6, 8, 10)
    }

    @Test
    fun testFilterNot() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.filterNot { it % 2 == 0 }.observeForever(observer)
        (1..10).forEach { data.value = it }

        observer.assertEquals(1, 3, 5, 7, 9)
    }

    @Test
    fun testFilterNotNull() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int?>()
        source.filterNotNull().observeForever(observer)
        listOf(1, null, 2, null, 3, 4).forEach { source.value = it }

        observer.assertEquals(1, 2, 3, 4)
    }

    @Test
    fun testFilterIsInstanceReified() {
        val observer = TestObserver<String>()
        val s = MutableLiveData<Any>()
        s.filterIsInstance<String>().observeForever(observer)
        s.value = "1"
        s.value = 2
        s.value = "3"
        s.value = 4L
        s.value = "5"

        observer.assertEquals("1", "3", "5")
    }

    @Test
    fun testFilterIsInstance() {
        val observer = TestObserver<String>()
        val s = MutableLiveData<Any>()
        s.filterIsInstance(String::class.java).observeForever(observer)
        s.value = "1"
        s.value = 2
        s.value = "3"
        s.value = 4L
        s.value = "5"

        observer.assertEquals("1", "3", "5")
    }
}
