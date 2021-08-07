package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class MergeTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private fun createTest(size: Int, func: (List<LiveData<out Int>>) -> LiveData<out Int>) {
        val observer = TestObserver<Int>()
        val sources = (0 until size).map { MutableLiveData<Int>() }
        func(sources).observeForever(observer)
        sources.forEachIndexed { index, source ->
            source.value = index
        }
        sources.reversed().forEachIndexed { index, source ->
            source.value = -index
        }

        observer.assertEquals((0 until size).toList() + (0 until size).map { -it })
    }

    @Test
    fun testMergeVararg() {
        createTest(2) { (s0, s1) -> mergeArray(s0, s1) }
    }

    @Test
    fun testMergeList() {
        createTest(2) { (s0, s1) -> listOf(s0, s1).merge() }
    }

    @Test
    fun testMergeArray() {
        createTest(2) { (s0, s1) -> arrayOf(s0, s1).merge() }
    }

    @Test
    fun testMergeWith() {
        createTest(2) { (s0, s1) -> s0.mergeWith(s1) }
    }

    @Test
    fun testMergeLiveDataPlusLiveData() {
        createTest(2) { (s0, s1) -> s0 + s1 }
    }

    @Test
    fun testMergeLiveDataPlusList() {
        createTest(2) { s -> s[0] + s.drop(1) }
    }

    @Test
    fun testMergeListPlusLiveData() {
        createTest(2) { s -> s.drop(1) + s[0] }
    }

    @Test
    fun testMergeLiveDataPlusArray() {
        createTest(2) { s -> s[0] + s.drop(1).toTypedArray() }
    }

    @Test
    fun testMergeArrayPlusLiveData() {
        createTest(2) { s -> s.drop(1).toTypedArray() + s[0] }
    }

    @Test
    fun testMerge1() {
        createTest(2) { s -> merge(s[0], s[1]) }
    }

    @Test
    fun testMerge2() {
        createTest(3) { s -> merge(s[0], s[1], s[2]) }
    }

    @Test
    fun testMerge3() {
        createTest(4) { s -> merge(s[0], s[1], s[2], s[3]) }
    }

    @Test
    fun testMerge4() {
        createTest(5) { s -> merge(s[0], s[1], s[2], s[3], s[4]) }
    }

    @Test
    fun testMerge5() {
        createTest(6) { s -> merge(s[0], s[1], s[2], s[3], s[4], s[5]) }
    }

    @Test
    fun testMerge6() {
        createTest(7) { s -> merge(s[0], s[1], s[2], s[3], s[4], s[5], s[6]) }
    }

    @Test
    fun testMerge7() {
        createTest(8) { s -> merge(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]) }
    }

    @Test
    fun testMerge8() {
        createTest(9) { s -> merge(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8]) }
    }

    @Test
    fun testMerge9() {
        createTest(10) { s -> merge(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9]) }
    }
}
