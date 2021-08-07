package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test

class CombineLatestTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private fun createTest(size: Int, func: (List<LiveData<out String>>, combiner: (Iterable<String>) -> String) -> LiveData<out String>) {
        val observer = TestObserver<String>()
        val sources = (0 until size).map { MutableLiveData<String>() }

        val combiner = { values: Iterable<String> ->
            println(values)
            values.joinToString("")
        }
        func(sources, combiner).observeForever(observer)
        sources.forEachIndexed { index, source ->
            source.value = index.toString()
        }
        sources.reversed().forEachIndexed { index, source ->
            source.value = ('a' + size - 1 - index).toString()
        }

        val seq = { index: Int ->
            (0 until size).map { position ->
                if (position < size - index) {
                    position.toString()
                } else {
                    ('a' + position).toString()
                }
            }
        }

        observer.assertEquals((0..size).map(seq).map(combiner))
    }

    @Test
    fun testCombineList() {
        createTest(2) { sources, combiner ->
            sources.combineLatest(combiner)
        }
    }

    @Test
    fun testCombineLatestArray() {
        createTest(2) { sources, combiner ->
            sources.toTypedArray().combineLatest(combiner)
        }
    }

    @Test
    fun testCombineLatestWith() {
        createTest(2) { (s0, s1), combiner ->
            s0.combineLatestWith(s1) { v1, v2 ->
                combiner(listOf(v1, v2))
            }
        }
    }

    @Test
    fun testCombineLatestWithPair() {
        createTest(2) { (s0, s1), combiner ->
            s0.combineLatestWith(s1).map { (v1, v2) ->
                combiner(listOf(v1, v2))
            }
        }
    }

    @Test
    fun testCombineLatestPair() {
        createTest(2) { (s0, s1), combiner ->
            combineLatest(s0, s1).map { (v1, v2) ->
                combiner(listOf(v1, v2))
            }
        }
    }

    @Test
    fun testCombineLatestTriple() {
        createTest(3) { (s0, s1, s2), combiner ->
            combineLatest(s0, s1, s2).map { (v1, v2, v3) ->
                combiner(listOf(v1, v2, v3))
            }
        }
    }

    @Test
    fun testCombineLatest1() {
        createTest(2) { s, combiner ->
            combineLatest(s[0], s[1]) { v0, v1 ->
                combiner(listOf(v0, v1))
            }
        }
    }

    @Test
    fun testCombineLatest2() {
        createTest(3) { s, combiner ->
            combineLatest(s[0], s[1], s[2]) { v0, v1, v2 ->
                combiner(listOf(v0, v1, v2))
            }
        }
    }

    @Test
    fun testCombineLatest3() {
        createTest(4) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3]) { v0, v1, v2, v3 ->
                combiner(listOf(v0, v1, v2, v3))
            }
        }
    }

    @Test
    fun testCombineLatest4() {
        createTest(5) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4]) { v0, v1, v2, v3, v4 ->
                combiner(listOf(v0, v1, v2, v3, v4))
            }
        }
    }

    @Test
    fun testCombineLatest5() {
        createTest(6) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5]) { v0, v1, v2, v3, v4, v5 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5))
            }
        }
    }

    @Test
    fun testCombineLatest6() {
        createTest(7) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6]) { v0, v1, v2, v3, v4, v5, v6 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6))
            }
        }
    }

    @Test
    fun testCombineLatest7() {
        createTest(8) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]) { v0, v1, v2, v3, v4, v5, v6, v7 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6, v7))
            }
        }
    }

    @Test
    fun testCombineLatest8() {
        createTest(9) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8]) { v0, v1, v2, v3, v4, v5, v6, v7, v8 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8))
            }
        }
    }

    @Test
    fun testCombineLatest9() {
        createTest(10) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9]) { v0, v1, v2, v3, v4, v5, v6, v7, v8, v9 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9))
            }
        }
    }
}
