package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class FilterTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testFilter() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.filter { it % 2 == 0 }.observeForever(observer)
        (1..10).forEach { source.value = it }

        observer.assertEquals(2, 4, 6, 8, 10)
    }

    @Test
    fun testFilterNot() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.filterNot { it % 2 == 0 }.observeForever(observer)
        (1..10).forEach { source.value = it }

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
        val source = MutableLiveData<Any>()
        source.filterIsInstance<String>().observeForever(observer)
        source.value = "1"
        source.value = 2
        source.value = "3"
        source.value = 4L
        source.value = "5"

        observer.assertEquals("1", "3", "5")
    }

    @Test
    fun testFilterIsInstance() {
        val observer = TestObserver<String>()
        val source = MutableLiveData<Any>()
        source.filterIsInstance(String::class.java).observeForever(observer)
        source.value = "1"
        source.value = 2
        source.value = "3"
        source.value = 4L
        source.value = "5"

        observer.assertEquals("1", "3", "5")
    }

    @Test
    fun testFilterIsInstancePrimitiveClass() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Any>()
        source.filterIsInstance(Int::class.java).observeForever(observer)
        source.value = "1"
        source.value = 2
        source.value = "3"
        source.value = 4L
        source.value = 5

        observer.assertEquals(2, 5)
    }
}
