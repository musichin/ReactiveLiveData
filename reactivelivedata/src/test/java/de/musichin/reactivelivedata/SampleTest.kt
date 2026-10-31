package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class SampleTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testSample() {
        val observer = TestObserver<Int?>()
        val source = MutableLiveData<Int?>()
        val sampler = MutableLiveData<Unit>()
        source.sample(sampler).observeForever(observer)
        sampler.value = Unit
        source.value = 1
        source.value = null
        sampler.value = Unit
        sampler.value = Unit
        source.value = 2
        sampler.value = Unit
        observer.assertEquals(null, 2)

        val sticky = TestObserver<Int?>()
        source.sample(sampler).observeForever(sticky)
        sticky.assertEquals(2)

        val self = TestObserver<Int?>()
        source.sample(source).observeForever(self)
        source.value = 3
        self.assertEquals(2, 3)
    }
}
