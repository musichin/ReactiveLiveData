package de.musichin.reactivelivedata

import androidx.lifecycle.Observer
import org.junit.Assert

class TestObserver<T> : Observer<T?> {
    var values: List<T?> = emptyList()
        private set

    override fun onChanged(t: T?) {
        values = values.plus(t)
    }

    fun assertNotEmpty() = Assert.assertFalse(values.isEmpty())
    fun assertEmpty() = Assert.assertTrue(values.isEmpty())
    fun assertSize(size: Int) = Assert.assertEquals(size, values.size)
    fun assertEquals(vararg values: Any?) = assertEquals(values.toList())
    fun assertEquals(values: List<Any?>) = Assert.assertEquals(values, this.values)

    val first: T? get() = values.firstOrNull()
    val last: T? get() = values.lastOrNull()
}
