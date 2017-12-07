package com.github.musichin.livedatautils

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MediatorTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Test
    fun testMediatorLiveData() {
        val numbers = MutableLiveData<Int>()
        val squaredNumbers = Transformations.map(Transformations.map(numbers) { it * it }) { it * 2}


        numbers.value = 1
        val observer1 = Observer<Int> {
//            assertEquals(1, it)
        }
        squaredNumbers.observeForever(observer1) // observer is called only with 1
        squaredNumbers.removeObserver(observer1)


        numbers.value = 2
        val observer2 = Observer<Int> {
//            assertEquals(4, it)
            println(it)
        }
        squaredNumbers.observeForever(observer2) // observer is called with 1 and 4
        squaredNumbers.removeObserver(observer2)
    }
}
