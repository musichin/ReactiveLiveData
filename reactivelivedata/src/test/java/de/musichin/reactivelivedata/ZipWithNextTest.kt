package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class ZipWithNextTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testZipWithNext() {
        val observer = TestObserver<Pair<Int?, Int?>>()
        val source = MutableLiveData<Int?>()
        source.zipWithNext().observeForever(observer)
        source.value = null
        observer.assertEmpty()

        source.value = 1
        source.value = 2
        observer.assertEquals(null to 1, 1 to 2)
    }

    @Test
    fun testZipWithNextTransform() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.zipWithNext { previous, current -> current - previous }.observeForever(observer)
        listOf(1, 3, 6, 10).forEach { source.value = it }

        observer.assertEquals(2, 3, 4)
    }
}
