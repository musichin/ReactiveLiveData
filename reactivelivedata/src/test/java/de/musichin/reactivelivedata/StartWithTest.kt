package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class StartWithTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testStartWithValue() {
        val source = liveDataOf(2).startWith(1)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(1, 2)
    }

    @Test
    fun testStartWithValues() {
        val source = liveDataOf(4).startWith(1, 2, 3)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(1, 2, 3, 4)

        val late = TestObserver<Int>()
        source.observeForever(late)
        late.assertEquals(4)
    }

    @Test
    fun testStartWithNoValues() {
        val origin = liveDataOf(4)
        val source = origin.startWith()
        assertNotSame(origin, source)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(4)
    }

    @Test
    fun testStartWithFunction() {
        val source = liveDataOf(2).startWith<Int> { 1 }
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(1, 2)
    }

    @Test
    fun testStartWithLiveData() {
        val startWith = lazyLiveData { 1 }
        val source = liveDataOf(2).startWith(startWith)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(1, 2)
    }
}
