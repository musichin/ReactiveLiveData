package de.musichin.reactivelivedata

import androidx.lifecycle.Observer
import org.junit.Assert

class TestObserver<T> : Observer<T> {
    var values: List<T> = emptyList()
        private set

    override fun onChanged(value: T) {
        values = values.plus(value)
    }

    fun assertEmpty() = Assert.assertTrue(values.isEmpty())

    fun assertEquals(vararg values: T) = assertValues(values.toList())

    fun assertValues(values: List<T>) = Assert.assertEquals(values, this.values)

    fun clean() {
        values = emptyList()
    }
}
