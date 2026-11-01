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
    fun testEmptyLiveData() {
        val observer = TestObserver<String>()
        emptyLiveData<String>().observeForever(observer)

        observer.assertEmpty()
    }

    @Test
    fun testLiveDataOf() {
        val observer = TestObserver<String>()
        liveDataOf("test").observeForever(observer)

        observer.assertEquals("test")
    }

    @Test
    fun testValueToLiveData() {
        val observer = TestObserver<String>()
        "test".toLiveData().observeForever(observer)

        observer.assertEquals("test")
    }

    @Test
    fun testLazyLiveData() {
        val source = lazyLiveData { "test" }
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

    @Test
    fun testLazyLiveDataCalledOnce() {
        var counter = 0
        val source = lazyLiveData { counter++ }
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEquals(0)
        observer.clean()

        source.removeObserver(observer)
        source.observeForever(observer)
        observer.assertEquals(0)
    }
}
