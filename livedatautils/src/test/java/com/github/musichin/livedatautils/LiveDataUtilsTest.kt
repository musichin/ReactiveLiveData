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
        LiveDataUtils.never<String>().observeForever(observer)

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
        LiveDataUtils.just("test").map { it.length }.observeForever(observer)

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

    @Test
    fun testDistinctUntilChangedWithKeySelector() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.distinctUntilChanged { it % 2 == 0 }.observeForever(observer)
        listOf(1, 1, 2, 3, 5, 7, 2, 4, 1, 5).forEach { data.value = it }

        observer.assertEquals(1, 2, 3, 2, 1)
    }

    @Test
    fun testMerge() {
        val observer = TestObserver<Int>()
        val data1 = MutableLiveData<Int>()
        val data2 = MutableLiveData<Int>()
        LiveDataUtils.merge(data1, data2).observeForever(observer)
        data1.value = 1
        data2.value = 2
        data2.value = 3
        data1.value = 4
        data2.value = 5

        observer.assertEquals(1, 2, 3, 4, 5)
    }

    @Test
    fun testCombineLatestPair() {
        val observer = TestObserver<String>()
        val s1 = MutableLiveData<Int>()
        val s2 = MutableLiveData<String>()
        LiveDataUtils.combineLatest(s1, s2) { t1, t2 -> "$t1$t2" }.observeForever(observer)
        s1.value = 1
        s2.value = "a"
        s2.value = "b"
        s1.value = 4
        s2.value = "c"

        observer.assertEquals("1a", "1b", "4b", "4c")
    }

    @Test
    fun testCombineLatestTriple() {
        val observer = TestObserver<String>()
        val s1 = MutableLiveData<Int>()
        val s2 = MutableLiveData<String>()
        val s3 = MutableLiveData<String>()
        LiveDataUtils.combineLatest(s1, s2, s3) { t1, t2, t3 -> "$t1$t2$t3" }.observeForever(observer)
        s2.value = "#"
        s1.value = 1
        s3.value = "a"
        s3.value = "b"
        s1.value = 4
        s2.value = "!"
        s3.value = "c"

        observer.assertEquals("1#a", "1#b", "4#b", "4!b", "4!c")
    }
}
