package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class ScanTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testScan() {
        val observer = TestObserver<String>()
        val source = MutableLiveData<Int>()
        val scanned = source.scan("") { acc, value -> acc + value }
        scanned.observeForever(observer)
        observer.assertEquals("")

        source.value = 1
        source.value = 2
        source.value = 3
        observer.assertEquals("", "1", "12", "123")

        scanned.removeObserver(observer)
        observer.clean()
        scanned.observeForever(observer)
        source.value = 4
        observer.assertEquals("123", "1234")
    }

    @Test
    fun testScanWithoutInitial() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.scan { acc, value -> acc + value }.observeForever(observer)
        observer.assertEmpty()

        source.value = 1
        source.value = 2
        source.value = 3
        observer.assertEquals(1, 3, 6)
    }
}
