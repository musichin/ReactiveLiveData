package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test

class CastTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testCast() {
        val source = liveData<Any> { "test" }
        val observer = TestObserver<String>()
        source.cast(String::class.java).observeForever(observer)
        observer.assertEquals("test")
    }

    @Test
    fun testCastReified() {
        val source = liveData<Any> { "test" }
        val observer = TestObserver<String>()
        source.cast<String>().observeForever(observer)
        observer.assertEquals("test")
    }
}
