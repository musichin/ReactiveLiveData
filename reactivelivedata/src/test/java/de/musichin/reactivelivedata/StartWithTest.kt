package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class StartWithTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testStartWithValue() {
        val source = liveData(2).startWith(1)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testStartWithFunction() {
        val source = liveData(2).startWith<Int> { 1 }
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }

    @Test
    fun testStartWithLiveData() {
        val startWith = liveData { 1 }
        val source = liveData(2).startWith(startWith)
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(listOf(1, 2))
    }
}
