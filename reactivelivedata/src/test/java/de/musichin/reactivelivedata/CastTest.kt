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
        val source = lazyLiveData<Any> { "test" }
        val observer = TestObserver<String>()
        source.cast(String::class.java).observeForever(observer)
        observer.assertEquals("test")
    }

    @Test
    fun testCastPrimitiveClass() {
        val source = lazyLiveData<Any> { 42 }
        val observer = TestObserver<Int>()
        source.cast(Int::class.java).observeForever(observer)
        observer.assertEquals(42)
    }

    @Test
    fun testCastReified() {
        val source = lazyLiveData<Any> { "test" }
        val observer = TestObserver<String>()
        source.cast<String>().observeForever(observer)
        observer.assertEquals("test")
    }
}
