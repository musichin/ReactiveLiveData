package de.musichin.reactivelivedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class CombineLatestTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private fun createTest(
        size: Int,
        func: (List<LiveData<out String>>, combiner: (Iterable<String>) -> String) -> LiveData<String>,
    ) {
        val observer = TestObserver<String>()
        val sources = (0 until size).map { MutableLiveData<String>() }

        val combiner = { values: Iterable<String> ->
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

        observer.assertValues((0..size).map(seq).map(combiner))
    }

    @Test
    fun testCombineLatestSkipsValues() {
        val source0 = MutableLiveData<String?>()
        val source1 = MutableLiveData<Int>()
        val source = listOf(source0, source1).combineLatest { it.joinToString("") }
        assertNull(source.value)

        val observer = TestObserver<String>()
        source.observeForever(observer)

        source0.value = "a"
        source0.value = "b"
        source0.value = "c"
        assertNull(source.value)

        source1.value = 4
        observer.assertEquals("c4")
    }

    @Test
    fun testCombineLatestEmpty() {
        val source = emptyList<LiveData<Int>>().combineLatest { it.sum() }
        assertNull(source.value)

        val observer = TestObserver<Int>()
        source.observeForever(observer)
        observer.assertEmpty()
    }

    @Test
    fun testCombineLatestSingle() {
        val source = listOf(lazyLiveData { "test_value" }).combineLatest { it.joinToString() }
        assertNull(source.value)

        val observer = TestObserver<String>()
        source.observeForever(observer)
        observer.assertEquals("test_value")
    }

    @Test
    fun testCombineLatestSameSource() {
        val source = MutableLiveData<String>()
        val observer = TestObserver<String>()
        combineLatest(source, source) { v0, v1 -> v0 + v1 }.observeForever(observer)

        source.value = "a"
        source.value = "b"
        observer.assertEquals("aa", "bb")
    }

    @Test
    fun testCombineLatestListWithDuplicates() {
        val source0 = MutableLiveData<String>()
        val source1 = MutableLiveData<String>()
        val observer = TestObserver<String>()
        listOf(source0, source1, source0).combineLatest { it.joinToString("") }.observeForever(observer)

        source0.value = "a"
        source1.value = "1"
        source0.value = "b"
        observer.assertEquals("a1a", "b1b")
    }

    @Test
    fun testCombineLatestArrayIterable() {
        createTest(2) { sources, combiner ->
            sources.toTypedArray().asIterable().combineLatest(combiner)
        }
    }

    @Test
    fun testCombineLatestList() {
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
    fun testCombineLatest2() {
        createTest(2) { s, combiner ->
            combineLatest(s[0], s[1]) { v0, v1 ->
                combiner(listOf(v0, v1))
            }
        }
    }

    @Test
    fun testCombineLatest3() {
        createTest(3) { s, combiner ->
            combineLatest(s[0], s[1], s[2]) { v0, v1, v2 ->
                combiner(listOf(v0, v1, v2))
            }
        }
    }

    @Test
    fun testCombineLatest4() {
        createTest(4) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3]) { v0, v1, v2, v3 ->
                combiner(listOf(v0, v1, v2, v3))
            }
        }
    }

    @Test
    fun testCombineLatest5() {
        createTest(5) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4]) { v0, v1, v2, v3, v4 ->
                combiner(listOf(v0, v1, v2, v3, v4))
            }
        }
    }

    @Test
    fun testCombineLatest6() {
        createTest(6) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5]) { v0, v1, v2, v3, v4, v5 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5))
            }
        }
    }

    @Test
    fun testCombineLatest7() {
        createTest(7) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6]) { v0, v1, v2, v3, v4, v5, v6 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6))
            }
        }
    }

    @Test
    fun testCombineLatest8() {
        createTest(8) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]) { v0, v1, v2, v3, v4, v5, v6, v7 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6, v7))
            }
        }
    }

    @Test
    fun testCombineLatest9() {
        createTest(9) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8]) { v0, v1, v2, v3, v4, v5, v6, v7, v8 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8))
            }
        }
    }

    @Test
    fun testCombineLatest10() {
        createTest(10) { s, combiner ->
            combineLatest(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9]) { v0, v1, v2, v3, v4, v5, v6, v7, v8, v9 ->
                combiner(listOf(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9))
            }
        }
    }

    @Test
    fun testWithLatestFrom() {
        val source = MutableLiveData<String>()
        val other = MutableLiveData<Int?>()
        val observer = TestObserver<String>()
        source.withLatestFrom(other) { v0, v1 -> v0 + v1 }.observeForever(observer)

        source.value = "a"
        other.value = null
        other.value = 1
        observer.assertEmpty()

        source.value = "b"
        other.value = 2
        other.value = null
        source.value = "c"
        observer.assertEquals("b1", "cnull")
    }

    @Test
    fun testWithLatestFromPair() {
        val source = MutableLiveData("a")
        val other = MutableLiveData(1)
        val observer = TestObserver<Pair<String, Int>>()
        source.withLatestFrom(other).observeForever(observer)
        observer.assertEquals("a" to 1)

        source.value = "b"
        observer.assertEquals("a" to 1, "b" to 1)
    }

    @Test
    fun testWithLatestFromSameSource() {
        val source = MutableLiveData<String>()
        val observer = TestObserver<String>()
        source.withLatestFrom(source) { v0, v1 -> v0 + v1 }.observeForever(observer)

        source.value = "a"
        source.value = "b"
        observer.assertEquals("aa", "bb")
    }
}
