package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test

class MapTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testMap() {
        val observer = TestObserver<Int>()
        liveData("test").map { it.length }.observeForever(observer)

        observer.assertEquals(4)
    }
}
