package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class LiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testEmpty() {
        val observer = TestObserver<String>()
        liveData<String>().observeForever(observer)

        observer.assertEmpty()
    }

    @Test
    fun testJust() {
        val observer = TestObserver<String>()
        liveData("test").observeForever(observer)

        observer.assertEquals("test")
    }

    @Test
    fun testToLiveDataFunction() {
        val source = liveData { "test" }
        assertNull(source.value)

        val observer = TestObserver<String>()
        source.observeForever(observer)
        observer.assertEquals("test")
    }

    @Test
    fun testFunctionToLiveData() {
        val source = { "test" }.toLiveData()
        assertNull(source.value)

        val observer = TestObserver<String>()
        source.observeForever(observer)
        observer.assertEquals("test")
    }
}
