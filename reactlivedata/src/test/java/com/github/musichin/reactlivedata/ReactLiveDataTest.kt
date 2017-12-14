package com.github.musichin.reactlivedata

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ReactLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testEmpty() {
        val observer = TestObserver<String>()
        ReactLiveData.never<String>().observeForever(observer)

        observer.assertEmpty()
    }

    @Test
    fun testJust() {
        val observer = TestObserver<String>()
        ReactLiveData.just("test").observeForever(observer)

        observer.assertEquals("test")
    }

    @Test
    fun testCreate() {
        val source = ReactLiveData.create { "test" }
        assertNull(source.value)

        val observer = TestObserver<String>()
        source.observeForever(observer)
        observer.assertEquals("test")
    }

    @Test
    fun testStartWithFunction() {
        val source = ReactLiveData.just(2).startWith<Int> { 1 }
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testStartWithLiveData() {
        val startWith = ReactLiveData.create { 1 }
        val source = ReactLiveData.just(2).startWith(startWith)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testMap() {
        val observer = TestObserver<Int>()
        ReactLiveData.just("test").map { it.length }.observeForever(observer)

        observer.assertEquals(4)
    }

    @Test
    fun testFilter() {
        val observer = TestObserver<Int>()
        val data = MutableLiveData<Int>()
        data.filter { it % 2 == 0 }.observeForever(observer)
        (1..10).forEach { data.value = it }

        observer.assertEquals(2, 4, 6, 8, 10)
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
    fun testDistinct() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.distinct().observeForever(observer)
        listOf(1, 1, 2, 3, 1, 3, 4, 5, 5, 2, 4, 5, 6).forEach { source.value = it }

        observer.assertEquals(1, 2, 3, 4, 5, 6)
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
        source.distinctUntilChanged { it % 2 == 0 }.observeForever(observer)
        listOf(1, 1, 2, 3, 5, 7, 2, 4, 1, 5).forEach { source.value = it }

        observer.assertEquals(1, 2, 3, 2, 1)
    }

    @Test
    fun testMerge() {
        val observer = TestObserver<Int>()
        val source1 = MutableLiveData<Int>()
        val source2 = MutableLiveData<Int>()
        ReactLiveData.merge(source1, source2).observeForever(observer)
        source1.value = 1
        source2.value = 2
        source2.value = 3
        source1.value = 4
        source2.value = 5

        observer.assertEquals(1, 2, 3, 4, 5)
    }

    @Test
    fun testCombineLatestPair() {
        val observer = TestObserver<String>()
        val s1 = MutableLiveData<Int>()
        val s2 = MutableLiveData<String>()
        ReactLiveData.combineLatest(s1, s2) { t1, t2 -> "$t1$t2" }.observeForever(observer)
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
        ReactLiveData.combineLatest(s1, s2, s3) { t1, t2, t3 -> "$t1$t2$t3" }.observeForever(observer)
        s2.value = "#"
        s1.value = 1
        s3.value = "a"
        s3.value = "b"
        s1.value = 4
        s2.value = "!"
        s3.value = "c"

        observer.assertEquals("1#a", "1#b", "4#b", "4!b", "4!c")
    }

    @Test
    fun testOnValue() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()

        val onValueCollection = mutableListOf<Int>()
        val onValueCollector: (Int) -> Unit = {
            onValueCollection.add(it)
        }
        val monitoredSource = source.doOnValue(onValueCollector)
        monitoredSource.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(1, 2, 3)
        assertEquals(listOf(1, 2, 3), onValueCollection)
    }

    @Test
    fun testOnActiveAndOnInactive() {
        val observer = TestObserver<Int>()
        var active = false
        val source = MutableLiveData<Int>()
        val monitoredSource = source.doOnActive { active = true }.doOnInactive { active = false }
        monitoredSource.observeForever(observer)
        assertTrue(active)
        monitoredSource.removeObserver(observer)
        assertFalse(active)
        monitoredSource.observeForever(observer)
        assertTrue(active)
    }
}
