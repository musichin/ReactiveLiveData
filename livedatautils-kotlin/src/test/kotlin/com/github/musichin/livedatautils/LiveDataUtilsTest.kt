package com.github.musichin.livedatautils

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class LiveDataUtilsTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testEmpty() {
        val observer = TestObserver<String>()
        LiveDataUtils.empty<String>().observeForever(observer)

        observer.assertEmpty()
    }

    @Test
    fun testJust() {
        val observer = TestObserver<String>()
        LiveDataUtils.just("test").observeForever(observer)

        observer.assertSize(1)
        observer.assertEquals("test")
    }

    @Test
    fun testMap() {
        val observer = TestObserver<Int>()
        LiveDataUtils.just("test").map { it?.length }.observeForever(observer)

        observer.assertSize(1)
        observer.assertEquals(4)
    }

    @Test
    fun testFilter() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.filter { it % 2 == 0 }.observeForever(observer)
        (1..10).forEach { data.value = it }

        observer.assertSize(5)
        observer.assertEquals(2, 4, 6, 8, 10)
    }

    @Test
    fun testFilterNotNull() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int?>()
        data.filterNotNull().observeForever(observer)
        listOf(1, null, 2, null, 3, 4).forEach { data.value = it }

        observer.assertEquals(1, 2, 3, 4)
    }

    @Test
    fun testDistinctUntilChanged() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.distinctUntilChanged().observeForever(observer)
        listOf(1, 1, 2, 3, 3, 3, 4, 5, 5, 6).forEach { data.value = it }

        observer.assertEquals(1, 2, 3, 4, 5, 6)
    }
}
