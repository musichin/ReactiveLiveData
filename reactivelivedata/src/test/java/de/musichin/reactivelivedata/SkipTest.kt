package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test

class SkipTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testSkipUntil() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.skipUntil { it >= 3 }.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 1
        source.value = 5

        observer.assertEquals(3, 1, 5)
    }

    @Test
    fun testSkipUntilOther() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        val other = MutableLiveData<String?>()
        source.skipUntil(other).observeForever(observer)
        source.value = 1
        source.value = 2
        other.value = null
        source.value = 3
        other.value = "x"
        source.value = 4
        observer.assertEquals(3, 4)

        val sticky = TestObserver<Int>()
        source.skipUntil(other).observeForever(sticky)
        sticky.assertEquals(4)

        val self = TestObserver<Int>()
        source.skipUntil(source).observeForever(self)
        source.value = 5
        self.assertEquals(4, 5)
    }

    @Test
    fun testSkipWhile() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.skipWhile { it < 3 }.observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 1
        source.value = 5

        observer.assertEquals(3, 1, 5)
    }

    @Test
    fun testSkip() {
        val observer = TestObserver<Int>()
        val source = MutableLiveData<Int>()
        source.skip(2).observeForever(observer)
        source.value = 1
        source.value = 2
        source.value = 3
        source.value = 4
        source.value = 5

        observer.assertEquals(3, 4, 5)
    }

    @Test
    fun testSkipRejectsNegativeCount() {
        assertThrows(IllegalArgumentException::class.java) {
            MutableLiveData<Int>().skip(-1)
        }
    }
}
