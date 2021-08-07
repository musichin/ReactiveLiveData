package de.musichin.reactivelivedata

import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Test

class HideTest {
    @Test
    fun testHide() {
        val source = MutableLiveData<String>()

        val hidden = source.hide()

        assertEquals(source, hidden)
    }
}
