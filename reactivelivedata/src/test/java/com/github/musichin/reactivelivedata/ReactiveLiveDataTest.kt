package com.github.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ReactiveLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testEmpty() {
        val observer = TestObserver<String>()
        ReactiveLiveData.never<String>().observeForever(observer)

        observer.assertEmpty()
    }

    @Test
    fun testJust() {
        val observer = TestObserver<String>()
        ReactiveLiveData.just("test").observeForever(observer)

        observer.assertEquals("test")
    }

    @Test
    fun testCreate() {
        val source = ReactiveLiveData.create { "test" }
        assertNull(source.value)

        val observer = TestObserver<String>()
        source.observeForever(observer)
        observer.assertEquals("test")
    }

    @Test
    fun testBuffer() {
        val source = MutableLiveData<Int>()
        val observer = TestObserver<List<Int>>()
        source.buffer(2).observeForever(observer)
        for (i in 0..9) source.value = i
        observer.assertEquals(listOf(listOf(0, 1), listOf(2, 3), listOf(4, 5), listOf(6, 7), listOf(8, 9)))
    }

    @Test
    fun testBufferWithSkip() {
        val source = MutableLiveData<Int>()
        val observer = TestObserver<List<Int>>()
        source.buffer(2, 3).observeForever(observer)
        for (i in 0..9) source.value = i
        observer.assertEquals(listOf(listOf(0, 1), listOf(3, 4), listOf(6, 7)))
    }

    @Test
    fun testCast() {
        val source = ReactiveLiveData.create<Any> { "test" }
        val observer = TestObserver<String>()
        source.cast(String::class.java).observeForever(observer)
        observer.assertEquals("test")
    }

    @Test
    fun testFirst() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.first().observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(listOf(1))
    }

    @Test
    fun testTake() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.take(2).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3

        observer.assertEquals(listOf(1, 2))
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

        observer.assertEquals(listOf(1, 2, 3))
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

        observer.assertEquals(listOf(1, 2, 3))
    }

    @Test
    fun testStartWithFunction() {
        val source = ReactiveLiveData.just(2).startWith<Int> { 1 }
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testStartWithLiveData() {
        val startWith = ReactiveLiveData.create { 1 }
        val source = ReactiveLiveData.just(2).startWith(startWith)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testMap() {
        val observer = TestObserver<Int>()
        ReactiveLiveData.just("test").map { it.length }.observeForever(observer)

        observer.assertEquals(4)
    }

    @Test
    fun testSwitchLatest() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<LiveData<Int>>()
        source.switchLatest().observeForever(observer)
        (1..10).forEach { source.value = it.toLiveData() }

        observer.assertEquals(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
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
        ReactiveLiveData.merge(source1, source2).observeForever(observer)
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
        ReactiveLiveData.combineLatest(s1, s2) { t1, t2 -> "$t1$t2" }.observeForever(observer)
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
        ReactiveLiveData.combineLatest(s1, s2, s3) { t1, t2, t3 -> "$t1$t2$t3" }.observeForever(observer)
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
