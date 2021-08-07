package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.math.abs

class DistinctTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testDistinct() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.distinct().observeForever(observer)
        listOf(1, 1, 2, 3, 1, 3, 4, 5, 5, 2, 4, 5, 6).forEach { source.value = it }

        observer.assertEquals(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun testDistinctKeyFunction() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.distinct { value -> abs(value) }.observeForever(observer)
        listOf(0, 1, -1, 2, -2).forEach { source.value = it }

        observer.assertEquals(0, 1, 2)
    }

    @Test
    fun testDistinctUntilChanged() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.distinctUntilChanged().observeForever(observer)
        listOf(1, 1, 2, 3, 3, 3, 4, 5, 5, 6).forEach { data.value = it }

        observer.assertEquals(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun testDistinctUntilChangedWithKeySelector() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.distinctUntilChanged { value -> value % 2 == 0 }.observeForever(observer)
        listOf(1, 1, 2, 3, 5, 7, 2, 4, 1, 5).forEach { source.value = it }

        observer.assertEquals(1, 2, 3, 2, 1)
    }

    @Test
    fun testDistinctUntilChangedWithComparer() {
        class A
        val observer = TestObserver<A>()
        val source = MutableLiveData<A>()
        source.distinctUntilChanged { v1, v2 -> v1 === v2 }.observeForever(observer)

        val a1 = A()
        val a2 = A()
        val a3 = A()
        val a4 = A()
        listOf(a1, a2, a2, a3, a4, a4, a4).forEach { source.value = it }

        observer.assertEquals(a1, a2, a3, a4)
    }
}
